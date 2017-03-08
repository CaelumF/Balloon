package com.gmail.caelum119.balloon.client.render.a3d

import com.ardor3d.math.ColorRGBA
import com.ardor3d.math.Quaternion
import com.ardor3d.math.Vector3
import com.ardor3d.renderer.state.CullState
import com.ardor3d.renderer.state.MaterialState
import com.ardor3d.scenegraph.Spatial
import com.ardor3d.scenegraph.shape.Quad
import com.ardor3d.ui.text.BasicText
import com.ardor3d.util.ReadOnlyTimer
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.gmail.caelum119.balloon.client.render.a3d.impl.A3DNodeImpl
import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonNode
import com.gmail.caelum119.balloon.world.scenegraph.visual.ClientEngine
import java.util.*


/**
 * First created 5/11/2016 in Client
 * Renders the world as described by the local client engine
 */
class A3DVBOWorldRenderer(val clengine: ClientEngine) : Ardor3DRendererBase() {
    override var engineInstance = clengine
    override var rendererName = "a3dVSG"
    override var rootBalloonBalloonNode: BalloonNode = A3DNodeImpl()

    private var frameRateLabel: BasicText? = null
    private var frames = 0
    private var startTime = System.currentTimeMillis()

    val tasks = ArrayList<Runnable>()

    override fun updateExample(timer: ReadOnlyTimer) {
        super.updateExample(timer)
        val now = System.currentTimeMillis()
        val dt = now - startTime
        if (dt > 1000) {
            val fps = Math.round(1e3 * frames / dt)
            frameRateLabel!!.text = "FPS: $fps, TPS(fixme), Vertices: ${_root.children.size * 32 * 2}"
            startTime = now
            frames = 0
        }

        frames++
        for (task in tasks) {
            task.run()
        }
        tasks.removeAll { true }
    }

    fun visualiseCollisions() {
        for (chunk in displayChunks) {
            for (collisionShape in chunk.physicsArea.dynamicsWorld.collisionObjectArray) {
                when (collisionShape.collisionShape) {
                    is StaticPlaneShape -> {
                        val planeConstant = (collisionShape.collisionShape as StaticPlaneShape).planeConstant.toDouble()
                        val spatial = Quad("100", 100.0, 100.0)
                        addMesh(spatial, Vector3(0.0, planeConstant, 0.0), 0.0, 90.0, 90.0, 0.0)
                    }
                }
            }
        }
    }

    fun addMesh(spatial: Spatial, translation: Vector3 = Vector3(0.0, 0.0, 0.0), rotX: Double, rotY: Double, rotZ: Double, rotAngle: Double) {
        spatial.translation = translation
        spatial.setRotation(Quaternion(rotX, rotY, rotZ, rotAngle))

        val materialState = MaterialState()
        materialState.ambient = ColorRGBA.ORANGE
        spatial.setRenderState(materialState)
        _root.attachChild(spatial)
    }

    override fun initRenderer() {
        _canvas.setTitle("VBOSpeedExample")

        //Add the sphere on the floor
        val cs = CullState()
        cs.cullFace = CullState.Face.Back
        cs.isEnabled = false
        _root.setRenderState(cs)

        //Set global, default material.
        val ms = MaterialState()
        ms.colorMaterial = MaterialState.ColorMaterial.Diffuse
        _root.setRenderState(ms)

//        //Not essential
//        val ts = TextureState()
//        ts.isEnabled = true
//        ts.texture = TextureManager.load("models/md2/df.jpg", Texture.MinificationFilter.Trilinear,
//                TextureStoreFormat.GuessCompressedFormat, true)
//
//        val sphereBase = Node("node")
//        _root.attachChild(sphereBase)
//
//        val nodeCopy = A3DModels.SPHERE.getNodeCopy()
//        nodeCopy.setScale(0.2)
//        sphereBase.attachChild(nodeCopy)


//        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.G), TriggerAction { source, inputStates, tpf ->
//            chunk.physicsArea.dynamicsWorld.stepSimulation(1 / 1000f, 1)
//        }))

//        sphereBase.sceneHints.dataMode = DataMode.VBO

        // Add fps display
        frameRateLabel = BasicText.createDefaultTextLabel("fpsLabel", "")
        frameRateLabel!!.setTranslation(5.0, _canvas.canvasRenderer.camera.height.toFloat() - 5f
                - frameRateLabel!!.height.toDouble(), 0.0)
        frameRateLabel!!.setTextColor(ColorRGBA.WHITE)
        frameRateLabel!!.sceneHints.orthoOrder = -1
        _root.attachChild(frameRateLabel)
        visualiseCollisions()

    }
}
