package com.gmail.caelum119.balloon.world.engine.components.physical

import com.gmail.caelum119.balloon.world.engine.components.PhysicalComponent
import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity


/**
 * First created 6/12/2016 in Engine
 */


class Magnetic(val attachedEntity: PhysicalEntity, val scalar: Double) : PhysicalComponent() {

    override fun physicsTick() {
        super.physicsTick()
        this.attachedEntity
        attachedEntity.residingChunk.let { residingChunk ->
//            for (entity in residingChunk.allEntities) {
//                if (!entity.equals(generalEntity)) {
//                    val distanceSquared: Double = entity.physicalEntity?.getLocationVector()!!.distanceSquared(appliedPhysicalEntity.getLocationVector()) + 0.00001
//                    val direction: Vector3 = appliedPhysicalEntity.getLocationVector().subtract(entity.physicalEntity?.getLocationVector(), Vector3()).normalizeLocal()
//                    appliedPhysicalEntity.applyForce(direction.divideLocal(distanceSquared).multiplyLocal(-scalar))
//                }
//            }
            for(entity: GeneralEntity in residingChunk.allEntities){
                if (entity !== attachedEntity){// entity is not the entity this component is attached to.
                    val distanceSquared = entity.allComponents
                }
            }
        }
    }
}