package com.gmail.caelum119.balloon.world.engine.components.perception

import com.gmail.caelum119.balloon.world.engine.components.VisualComponent
import com.gmail.caelum119.balloon.world.scenegraph.GeneralEntity

/**
 * First created 12/15/2016 in BalloonEngine
 * The perspective object serves as a viewpoint and/or hearpoint.
 * Anything that a player can see or hear from should use this component.
 * For performance reasons,
 */
class Perspective(attachedEntity: GeneralEntity) : VisualComponent(attachedEntity) {
    fun getSurroundingVisualEntities() = attachedEntity.residingChunk.allVisualEntities
}