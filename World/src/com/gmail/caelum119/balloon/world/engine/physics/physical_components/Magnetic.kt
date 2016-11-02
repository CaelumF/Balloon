package com.gmail.caelum119.balloon.world.engine.physics.physical_components

import com.ardor3d.math.Vector3
import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
import com.gmail.caelum119.scenegraph.Component


/**
 * First created 6/12/2016 in Engine
 */


class Magnetic(val generalEntity: GeneralEntity, val scalar: Double) : Component(generalEntity) {
    val appliedPhysicalEntity: PhysicalEntity = generalEntity.physicalEntity!!

    override fun physicsTick() {
        super.physicsTick()
        generalEntity.residingChunk.let { residingChunk ->
            for (entity in residingChunk.entities) {
                if (!entity.equals(generalEntity)) {
                    val distanceSquared: Double = entity.physicalEntity?.getLocationVector()!!.distanceSquared(appliedPhysicalEntity.getLocationVector()) + 0.00001
                    val direction: Vector3 = appliedPhysicalEntity.getLocationVector().subtract(entity.physicalEntity?.getLocationVector(), Vector3()).normalizeLocal()
                    appliedPhysicalEntity.applyForce(direction.divideLocal(distanceSquared).multiplyLocal(-scalar))
                }
            }
        }
    }
}