package com.gmail.caelum119.balloon.client.render.a3d.impl

import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonNode
import com.gmail.caelum119.balloon.world.scenegraph.visual.BalloonRenderer
import com.gmail.caelum119.balloon.world.scenegraph.visual.common.Quaternion
import com.gmail.caelum119.balloon.world.scenegraph.visual.common.Translation
import com.gmail.caelum119.utils.event.ListenerInterface
import java.util.*

/**
 * First created 1/20/2017 in BalloonEngine
 */
class A3DNodeImpl(override var renderer: BalloonRenderer?, override var engineInstance: EngineInstance?, var model:
A3DModels? = null) :
        BalloonNode {
    override var translation: Translation = A3DTranslation(0f, 0f, 0f)
    override var rotation: Quaternion = A3DQuaternion(0f, 0f, 0f, 0f)

    override val eventTrigger = BalloonNode.EventTypes()
    override val events = ListenerInterface(eventTrigger)
    override var parent: BalloonNode? = renderer?.rootNode
    override var children: ArrayList<BalloonNode> = ArrayList()

    constructor(parent: BalloonNode) : this(parent.renderer, parent.engineInstance) {
        this.parent = parent
    }
    constructor() : this(null, null)
}