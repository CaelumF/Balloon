package com.gmail.caelum119.balloon.world.engine.components

import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity

/**
 * First created 6/12/2016 in Engine
 *
 * TODO: Add Observer and decoupled ECS pattern with complimenting type-safe-builders capabilities
 */
open class Component(var attachedEntity: GeneralEntity) {
    var enabled = true

    init {

    }
}