package com.gmail.caelum119.balloon.client.render

import com.ardor3d.bounding.BoundingBox
import com.ardor3d.image.Texture
import com.ardor3d.image.TextureStoreFormat
import com.ardor3d.input.Key
import com.ardor3d.input.logical.InputTrigger
import com.ardor3d.input.logical.KeyPressedCondition
import com.ardor3d.input.logical.TriggerAction
import com.ardor3d.math.ColorRGBA
import com.ardor3d.math.Quaternion
import com.ardor3d.math.Vector3
import com.ardor3d.renderer.state.CullState
import com.ardor3d.renderer.state.MaterialState
import com.ardor3d.renderer.state.TextureState
import com.ardor3d.scenegraph.Node
import com.ardor3d.scenegraph.Spatial
import com.ardor3d.scenegraph.hint.DataMode
import com.ardor3d.scenegraph.shape.Quad
import com.ardor3d.scenegraph.shape.Sphere
import com.ardor3d.ui.text.BasicText
import com.ardor3d.util.ReadOnlyTimer
import com.ardor3d.util.TextureManager
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.gmail.caelum119.balloon.world.scenegraph.Chunk
import com.gmail.caelum119.balloon.world.scenegraph.VisualEntity
import java.util.*
import javax.vecmath.Vector3f


/**
 * First created 5/11/2016 in Client
 * Renders the world as described by the local client engine
 */
class A3DVBOWorldRenderer : Ardor3DRendererBase() {
    private var frameRateLabel: BasicText? = null
    private var frames = 0
    private var startTime = System.currentTimeMillis()

    private var testSphere: Sphere? = null

    lateinit var chunk: Chunk
    val renderableEntities = ArrayList<VisualEntity>()
    val tasks = ArrayList<Runnable>()

    override fun updateExample(timer: ReadOnlyTimer) {
        super.updateExample(timer)
        val now = System.currentTimeMillis()
        val dt = now - startTime
        if (dt > 1000) {
            val fps = Math.round(1e3 * frames / dt)
            frameRateLabel!!.text = "FPS: $fps, TPS: ${chunk.DTPS}, Vertices: ${_root.children.size * 32 * 2}"
            startTime = now
            frames = 0
        }

        for (entity in renderableEntities) {
            entity.updateVisualInformation()
        }

        frames++
        for (task in tasks) {
            task.run()
        }
        tasks.removeAll { true }
    }

    fun visualiseCollisions() {
        for (collisionShape in chunk.JBulletPhysics.dynamicsWorld.collisionObjectArray) {
            when (collisionShape.collisionShape) {
                is StaticPlaneShape -> {
                    val planeConstant = (collisionShape.collisionShape as StaticPlaneShape).planeConstant.toDouble()
                    val spatial = Quad("100", 100.0, 100.0)
                    addMesh(spatial, Vector3(0.0, planeConstant, 0.0), 0.0, 90.0, 90.0, 0.0)
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

    override fun initExample() {
        _canvas.setTitle("VBOSpeedExample")
        //Add the sphere on the floor
        val cs = CullState()
        cs.cullFace = CullState.Face.Back
        cs.isEnabled = false
        _root.setRenderState(cs)

        val ms = MaterialState()
        ms.colorMaterial = MaterialState.ColorMaterial.Diffuse
        _root.setRenderState(ms)

        val ts = TextureState()
        ts.isEnabled = true
        ts.texture = TextureManager.load("images/ardor3d_white_256.jpg", Texture.MinificationFilter.Trilinear,
                TextureStoreFormat.GuessCompressedFormat, true)

        val sphereBase = Node("node")
        _root.attachChild(sphereBase)

        val rand = Random(1337)
        //        for(int i = 0; i < 300; i++){
        //            final Sphere sphere = new Sphere("Sphere", 32, 32, 2);
        //            sphere.setRandomColors();
        //            sphere.setModelBound(new BoundingBox());
        //            sphere.setRenderState(ts);
        //            sphere.setTranslation(new Vector3(rand.nextDouble() * 100.0 - 50.0, rand.nextDouble() * 100.0 - 50.0, rand
        //                    .nextDouble() * 100.0 - 250.0));
        //
        //            sphereBase.attachChild(sphere);
        //        }

        testSphere = Sphere("Sphere", 32, 32, 1.0)
        testSphere!!.modelBound = BoundingBox()
        testSphere!!.setRenderState(ts)
        sphereBase.attachChild(testSphere)

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.SPACE), TriggerAction { source, inputStates, tpf ->
            tasks.add(Runnable {
                chunk.createSphere(Math.max(rand.nextFloat() * 4, 1f), Vector3f(0f + (rand.nextFloat() * 200), 30.0f * rand.nextFloat(), 200 * rand.nextFloat()))
            })
//            chunk.createSphere(10f, Vector3f(Random().nextFloat() * 400, 50.0f, Random().nextFloat() * 400))
        }))

        _logicalLayer.registerTrigger(InputTrigger(KeyPressedCondition(Key.G), TriggerAction { source, inputStates, tpf ->
            chunk.JBulletPhysics.dynamicsWorld.stepSimulation(1 / 1000f, 1)
        }))

        sphereBase.sceneHints.dataMode = DataMode.VBO

        // Add fps display
        frameRateLabel = BasicText.createDefaultTextLabel("fpsLabel", "")
        frameRateLabel!!.setTranslation(5.0, _canvas.canvasRenderer.camera.height.toFloat() - 5f
                - frameRateLabel!!.height.toDouble(), 0.0)
        frameRateLabel!!.setTextColor(ColorRGBA.WHITE)
        frameRateLabel!!.sceneHints.orthoOrder = -1
        _root.attachChild(frameRateLabel)

        chunk = Chunk(0, 0, 0)
        //When an entity with a visual presence is added the local JBI's scenegraph, add it to our list of renderable entities
        chunk.onEntityCreateListeners.add { entityAdded ->
            tasks.add(Runnable {
                entityAdded.visualEntity?.let {
                    //Create an associated visual entity stored here for rendering, using the visual information referenced by the engine entity
                    _root.attachChild(it.a3dNode)
                    renderableEntities.add(it)
                }
            })
        }
        visualiseCollisions()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            start(A3DVBOWorldRenderer::class.java)
        }
    }
}
