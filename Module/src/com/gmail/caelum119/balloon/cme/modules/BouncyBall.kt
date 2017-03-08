package com.gmail.caelum119.balloon.cme.modules

import com.gmail.caelum119.balloon.world.engine.components.PhysicalComponent
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity

/**
 * First created 1/29/2017 in BalloonEngine
 */
class BouncyBall(var physicalEntity: PhysicalEntity): PhysicalComponent(physicalEntity) {
    init {

    }

    override fun physicsTick() {
        super.physicsTick()
    }
}