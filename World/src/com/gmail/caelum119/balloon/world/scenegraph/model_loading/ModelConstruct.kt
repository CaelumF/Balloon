//package com.gmail.caelum119.balloon.world.scenegraph.model_loading
//
//import com.ardor3d.scenegraph.Spatial
//import com.bulletphysics.collision.dispatch.CollisionObject
//import com.gmail.caelum119.balloon.world.scenegraph.Chunk
//import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
//
///**
// * First created 6/6/2016 in Engine
// * Stores constructors for visual and physical objects, to retrieve a unique instance of a model from the same source without necessarily going through the effort to
// * fully process it again.
// * TODO: Replace this whole system to something similar but revised for the new plans
// */
//open class ModelConstruct<E>(val visualA3DConstructor: (argument: E) -> Spatial, val physicalJBConstructor: (argument: E) -> CollisionObject) {
//    //    /**
////     * Returns a copy of the A3D node associated with this construct by copying the last constructed [spatial].
////     */
////    @Deprecated("not sure if this is the path I want to take, might instead use A3D's makeCopy() inside the construct lambda")
////    fun getVisualNodeA3DCopy(reuseGeometricData: Boolean): Node {
////        Node().let { it.attachChild(spatial.makeCopy(reuseGeometricData)); return it }
////    }
//
//    fun getGeneralEntityCopy(constructionParameters: E, residingChunk: Chunk): PhysicalEntity {
//        val physicalEntity = PhysicalEntityImpl(residingChunk)
//        physicalEntity.jbObject.collisionShape = physicalJBConstructor.invoke(constructionParameters).collisionShape
//        return physicalEntity
//    }
//
//    open class ConstructorParameters(vararg variableParameters: Any) {
//        var parameters: Array<Any> = variableParameters as Array<Any>
//        init {
//
//        }
//    }
//}