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
import com.gmail.caelum119.balloon.client.ClientEngine
import com.gmail.caelum119.balloon.world.engine.components.perception.Perspective
import com.gmail.caelum119.balloon.world.scenegraph.Chunk
import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity
import java.util.*


/**
 * First created 5/11/2016 in Client
 * Renders the world as described by the local client engine
 */
class A3DVBOWorldRenderer(val clengine: ClientEngine) : Ardor3DRendererBase() {
    private var frameRateLabel: BasicText? = null
    private var frames = 0
    private var startTime = System.currentTimeMillis()

    private var testSphere: Sphere? = null

    lateinit var chunk: Chunk

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

        //Set global, default material.
        val ms = MaterialState()
        ms.colorMaterial = MaterialState.ColorMaterial.Diffuse
        _root.setRenderState(ms)

        //Not essential
        val ts = TextureState()
        ts.isEnabled = true
        ts.texture = TextureManager.load("images/ardor3d_white_256.jpg", Texture.MinificationFilter.Trilinear,
                TextureStoreFormat.GuessCompressedFormat, true)

        val sphereBase = Node("node")
        _root.attachChild(sphereBase)


        testSphere = Sphere("Sphere", 32, 32, 1.0)
        testSphere!!.modelBound = BoundingBox()
        testSphere!!.setRenderState(ts)
        sphereBase.attachChild(testSphere)



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

        //Temporary test code. Chunks should rarely be constructed by the client, little owe the Renderer.
        /*
        chunk = Chunk(0, 0, 0)
        //When an entity with a visual presence is added the local JBI's scenegraph, add it to our list of renderable allEntities
        chunk.onEntityCreateListeners.add { entityAdded ->
            tasks.add(Runnable {
                entityAdded.visualEntity?.let {
                    //Create an associated visual entity stored here for rendering, using the visual information referenced by the engine entity
                    _root.attachChild(it.a3dNode)
                    renderableEntities.add(it)
                }
            })
        }*/

        visualiseCollisions()

        /*
            Add listener to all chunks with at least one Perspective component and their surrounding chunks to attach-
            newly added(or transferred) visual entities to the root a3d node (as children)
        */
        for (chunk in clengine.chunks) {
            chunk.getComponentsByType(Perspective::class)
                    .forEach { persComponent -> //Every Perspective component in every Chunk V
                        persComponent.attachedEntity.events.addListener { newChunkE: GeneralEntity.EventTypes.E_CHUNK_CHANGED ->
                            for (visualEntity in newChunkE.newChunk.) {
                                visualEntity
                            }
                        }
                    }
        }

    }
}
