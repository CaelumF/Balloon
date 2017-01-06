package com.gmail.caelum119.balloon.world.engine.components

import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity

/**
 * First created 12/16/2016 in BalloonEngine
 * A component to attach to entities for client-side effects.
 * VisualComponents should only exist on client instances.
 */
open class VisualComponent(attachedEntity: GeneralEntity) : Component(attachedEntity) {

    open fun visualTick() {

    }
}