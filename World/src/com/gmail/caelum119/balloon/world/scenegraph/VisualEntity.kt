package com.gmail.caelum119.balloon.world.scenegraph


import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonNode
import java.util.*
import javax.vecmath.Quat4f

/**
 * First created 5/16/2016 in Client
 * Represents a entity that exists physically and is also visually available. Provides an interface to interact with the
 * renderer
 *
 * TODO: VisualEntities could have separate locations from GeneralEntities for interpolation purpose, JBullet's interpolation may not integrate efficiently.
 *
 */
abstract class VisualEntity(val physicalEntity: PhysicalEntity) :
        GeneralEntity(physicalEntity.residingChunk) {
    abstract val node: BalloonNode

    /**
     * Used to render node
     */

    val onVisualInfoUpdate = ArrayList<(updating: VisualEntity) -> Unit>()
    var inRenderingRange = false

    init {

    }

//    constructor(visualModel: ModelConstruct<Any>, partOf: GeneralEntity, constructArgs: ModelConstruct.ConstructorParameters) : this(visualModel.visualA3DConstructor.invoke(constructArgs), )

    /**
     * Updates the location of [node] to reflect the physical instance from [partOf]
     * TODO: Extrapolation from the information imposed on this object using this method. Interpolation would
     * increase latency
     */
    open fun updateSpatialInformation(physicalEntity: PhysicalEntity) {
        onVisualInfoUpdate.forEach { it.invoke(this) }
        val physicalTransform = physicalEntity.getTransform()
        val physicalRotation: Quat4f = physicalTransform.getRotation(Quat4f())

        node.translation.x = physicalTransform.origin.x
        node.translation.y = physicalTransform.origin.y
        node.translation.z = physicalTransform.origin.z

        node.rotation.x = physicalRotation.x
        node.rotation.y = physicalRotation.y
        node.rotation.z = physicalRotation.z
        node.rotation.w = physicalRotation.w
    }
}