//package com.gmail.caelum119.balloon.world.scenegraph.model_loading
//
//
//import com.ardor3d.image.Texture
//import com.ardor3d.image.TextureStoreFormat
//import com.ardor3d.renderer.state.TextureState
//import com.ardor3d.scenegraph.Node
//import com.ardor3d.util.TextureManager
//import com.bulletphysics.collision.shapes.SphereShape
//import com.bulletphysics.dynamics.RigidBody
//import com.bulletphysics.dynamics.RigidBodyConstructionInfo
//import java.util.*
//import javax.vecmath.Vector3f
//import com.ardor3d.scenegraph.shape.Sphere as A3DSphere
//
///**
// * First created 6/5/2016 in Engine
// * Stores models in a a3d compatible format
// * TODO: Revise/remove this whole system to reflect the new plans
// */
//object ModelService {
//    val fallRigidBodyCI = RigidBodyConstructionInfo(10f, null, SphereShape(32f), Vector3f(0f, 1f, 0f))
//    /**
//     * Returns a sphere contstruct constructed with
//     */
//
//    class SphereConstructParameters(val radius: Double, val vertices: Int, val mass: Float)
//    val sphereConstruct = ModelConstruct<SphereConstructParameters>({
//        val a3DSphere = com.ardor3d.scenegraph.shape.Sphere("", it.vertices, it.vertices, it.radius)
//        val ts = TextureState()
//        ts.isEnabled = true
//        ts.texture = TextureManager.load("images/pepe.jpg", Texture.MinificationFilter.Trilinear,
//                TextureStoreFormat.GuessCompressedFormat, true)
//        a3DSphere.setRenderState(ts)
//        return@ModelConstruct a3DSphere
//
//    }, {
//        val sphereShape = SphereShape(it.radius.toFloat())
//        val fallInertia = Vector3f(0.0f, 1.0f, 0.0f)
//        sphereShape.calculateLocalInertia(1f, fallInertia)
//
//        val fallRigidBodyCI = RigidBodyConstructionInfo(it.mass, null, sphereShape, fallInertia)
//        fallRigidBodyCI.friction = 0.5f
////        fallRigidBodyCI.angularDamping = 0.3f
////        fallRigidBodyCI.linearDamping = 0.05f
//        fallRigidBodyCI.restitution = 0.1f
//
//        val rigidBody = RigidBody(fallRigidBodyCI)
//        rigidBody.setSleepingThresholds(0.001f, 0.001f)
//        return@ModelConstruct rigidBody
//    })
//
//    init {
//
//    }
//
//    val models = HashMap<String, Node>()
//    fun getModel(key: String): Node {
//        return models[key]!!
//    }
//}