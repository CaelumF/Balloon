package com.gmail.caelum119.balloon.world.scenegraph.visual

import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.scenegraph.visual.common.Quaternion
import com.gmail.caelum119.balloon.world.scenegraph.visual.common.Translation
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface
import java.util.*

/**
 * First created 1/20/2017 in BalloonEngine
 */
interface BalloonNode {
    var parent: BalloonNode?
    var children: ArrayList<BalloonNode>
    val events: ListenerInterface
    val eventTrigger: EventTypes
    var engineInstance: EngineInstance?
    var renderer: BalloonRenderer?

    var translation: Translation
    var rotation: Quaternion

    fun addChild(node: BalloonNode){
        children.add(node)
        node.parent = this
        eventTrigger.triggerEvent(EventTypes.childAdded(node))
    }

    open class EventTypes() : EventCollection() {
        open class childAdded(node: BalloonNode): EventType()
    }
}