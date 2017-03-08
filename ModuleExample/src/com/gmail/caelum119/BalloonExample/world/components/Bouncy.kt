package com.gmail.caelum119.BalloonExample.world.components

import com.gmail.caelum119.balloon.world.engine.components.PhysicalComponent
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity

/**
 * First created 2/19/2017 in BalloonEngine
 */
class Bouncy(attachedToPhysicalEntity: PhysicalEntity) : PhysicalComponent(attachedToPhysicalEntity) {
    init {
//        attachedToPhysicalEntity.events.addListener { event: PhysicalEntityE -> }
        attachedToPhysicalEntity.events.addListener { event: PhysicalEntity.PhysicEvents.E_COLLISION_EVENT ->
            println("collided!")
        }
    }

    override fun physicsTick() {
        super.physicsTick()
    }
}