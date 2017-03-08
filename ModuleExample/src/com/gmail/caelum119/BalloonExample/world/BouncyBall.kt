package com.gmail.caelum119.BalloonExample.world

import com.gmail.caelum119.balloon.client.render.a3d.impl.A3DNodeImpl
import com.gmail.caelum119.balloon.world.scenegraph.Chunk
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
import com.gmail.caelum119.balloon.world.scenegraph.VisualEntity
import com.gmail.caelum119.BalloonExample.world.components.Bouncy

/**
 * First created 2/19/2017 in BalloonEngine
 */
class BouncyBall(residingChunk: Chunk): PhysicalEntity(residingChunk){

    /**
     * Subclass method of describing physical entities visually
     */
    class VisualCounterpart(physicalEntity: PhysicalEntity): VisualEntity(physicalEntity) {
        override val node = A3DNodeImpl()
    }

    override val visualCounterpart = VisualEntity::class.java

    init {
        addComponent(Bouncy(this))
    }
}