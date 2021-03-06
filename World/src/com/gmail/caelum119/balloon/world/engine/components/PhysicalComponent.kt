package com.gmail.caelum119.balloon.world.engine.components

import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity

/**
 * First created 12/16/2016 in BalloonEngine
 * A PhysicalComponent is a component that attaches to a PhysicalEntity and defines physical behavour for that entity.
 *
 * For purely visual affects use a VisualComponent
 * for visual affects that need to communicate with the physical aspect of an entity, use a
 */
open class PhysicalComponent(attachedToPhysicalEntity: PhysicalEntity): Component(attachedToPhysicalEntity) {
    open fun physicsTick() {

    }
}