package com.gmail.caelum119.balloon.world.scenegraph

import com.ardor3d.math.Quaternion
import com.ardor3d.scenegraph.Node
import com.ardor3d.scenegraph.Spatial
import com.gmail.caelum119.balloon.world.scenegraph.locationToXYZDouble
import com.gmail.caelum119.balloon.world.scenegraph.rotationToXYZWDouble
import java.util.*
import javax.vecmath.Quat4f

/**
 * First created 5/16/2016 in Client
 * Represents a entity that exists physically and is also visually available,
 *
 * TODO: VisualEntities could have separate locations from GeneralEntities for interpolation purpose, JBullet's interpolation may not integrate efficiently.
 *
 */
class VisualEntity constructor(visualModelInstance: Spatial, var partOf: GeneralEntity) {

    /**
     * Used to render a3dNode
     */
    val a3dNode: Node
    val onVisualInfoUpdate = ArrayList<(updating: VisualEntity) -> Unit>()

    init {
        a3dNode = Node("")
        a3dNode.attachChild(visualModelInstance)
    }

    constructor(visualModel: ModelConstruct<Any>, partOf: GeneralEntity, constructArgs: ModelConstruct.ConstructorParameters) : this(visualModel.visualA3DConstructor.invoke(constructArgs), partOf)

    /**
     * Updates the location of [a3dNode] to reflect the physical instance from [partOf]
     */
    fun updateVisualInformation() {
        onVisualInfoUpdate.forEach { it.invoke(this) }
        partOf.physicalEntity?.let {
            val physicalTransform = it.getTransform()
            val physicalRotation: Quat4f = physicalTransform.getRotation(Quat4f())
            val (x, y, z) = physicalTransform.locationToXYZDouble()
            val (rx, ry, rz, rw) = physicalRotation.rotationToXYZWDouble()

            a3dNode.setTranslation(x, y, z)
            a3dNode.setRotation(Quaternion(rx, ry, rz, rw))
        }
    }
}