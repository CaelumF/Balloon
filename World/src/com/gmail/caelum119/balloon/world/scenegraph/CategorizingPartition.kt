package com.gmail.caelum119.balloon.world.scenegraph

import com.gmail.caelum119.balloon.world.engine.components.Component

interface CategorizingPartition {
    val allT1: List<Any>
    val allT1ByType: Map<Class<Any>, List<Any>>

    val allT2: List<Any>
    val allT2ByType: Map<Class<Any>, List<Any>>

    fun addComponent(component: Component)
}