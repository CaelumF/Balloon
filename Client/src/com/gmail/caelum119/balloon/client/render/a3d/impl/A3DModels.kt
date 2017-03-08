package com.gmail.caelum119.balloon.client.render.a3d.impl

import com.ardor3d.extension.model.md2.Md2Importer
import com.ardor3d.image.Texture
import com.ardor3d.image.TextureStoreFormat
import com.ardor3d.math.MathUtils
import com.ardor3d.math.Quaternion
import com.ardor3d.math.Vector3
import com.ardor3d.renderer.state.TextureState
import com.ardor3d.scenegraph.Node
import com.ardor3d.scenegraph.controller.ComplexSpatialController
import com.ardor3d.util.TextureManager
import com.ardor3d.util.resource.ResourceLocatorTool
import com.gmail.caelum119.utils.files.FileIO

/**
 * First created 3/1/2017 in BalloonEngine
 * An enumeration of A3D models, their A3D nodes for copying and other information and data related to models
 */
val modelPath = FileIO.localPath + "/models/"
const val DEFAULT_MODEL_PATH = "models/md2/df.md2"

enum class A3DModels(val modelPath: String, val texturePath: String = modelPath.replaceAfterLast(".", "jpg")) {

    SPHERE(DEFAULT_MODEL_PATH),
    DEFAULT(DEFAULT_MODEL_PATH);

    val extension: String
    protected val node: Node
    val ts: TextureState


    init {
        //Determine extension
        val extensionStart = modelPath.indexOfLast { it == '.' }
        if (extensionStart != -1) {
            extension = modelPath.substring(extensionStart)
        } else extension = ""

        //Load model into node, depending on which type of model [path] points to
        val modelDirUrl = ResourceLocatorTool.getClassPathResource(A3DModels::class.java,
                "models/")
        val s = modelPath.replaceAfterLast(".", "jpg")
//        ts = TextureState().apply {
//            isEnabled = true
//            texture = TextureManager.load("models/md2/df.jpg", Texture.MinificationFilter.Trilinear,
//                    TextureStoreFormat.GuessCompressedFormat, true)
//        }
        ts = TextureState()
        ts.isEnabled = true
        ts.texture = TextureManager.load(modelPath.replaceAfterLast(".", "jpg"), Texture.MinificationFilter.Trilinear,
                TextureStoreFormat.GuessCompressedFormat, false)
        node = when (extension) {
            ".md2" -> {
                val md2Importer = Md2Importer()
                val md2DataStore = md2Importer.load(modelPath).apply {
                    controller.speed = 4.0
                    controller.repeatType = ComplexSpatialController.RepeatType.CYCLE
                }
                val model = md2DataStore.scene.apply {
                    setRenderState(ts)
                    // md2 models are usually z-up - switch to y-up
                    setRotation(Quaternion().fromAngleAxis(-MathUtils.HALF_PI, Vector3.UNIT_X))
                }
                val node = Node()

                node.attachChild(model)
                node
            }
            else -> throw Exception("No model ")
        }
    }

    fun getNodeCopy(): Node {
        return node
    }
}

