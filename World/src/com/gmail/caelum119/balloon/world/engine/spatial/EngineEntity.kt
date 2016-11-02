package com.gmail.caelum119.engine.spatial

import com.gmail.caelum119.engine.EngineComponent
import java.util.*

/**
 * First created 3/27/2016 in Engine
 * Basically anything. It does not have to occupy physical space, like a [PhysicalEntity]
 */
public open class EngineEntity {
  public val components: List<EngineComponent> = ArrayList()
}