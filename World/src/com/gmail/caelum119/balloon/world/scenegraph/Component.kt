package com.gmail.caelum119.scenegraph

import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
import com.gmail.caelum119.balloon.world.scenegraph.VisualEntity

/**
 * First created 6/12/2016 in Engine
 */
abstract class Component(var physicalEntity: PhysicalEntity?, var visualEntity: VisualEntity?) {
    constructor(generalEntity: GeneralEntity) : this(generalEntity.physicalEntity, generalEntity.visualEntity)

    open fun physicsTick(){

    }

    open fun visualTick(){

    }
}