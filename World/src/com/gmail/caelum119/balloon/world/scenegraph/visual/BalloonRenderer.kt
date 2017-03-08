package com.gmail.caelum119.balloon.world.scenegraph.visual

import com.gmail.caelum119.balloon.world.engine.components.perception.Perspective
import com.gmail.caelum119.balloon.world.scenegraph.CallbackCategorizingNSPImpl
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
import com.gmail.caelum119.balloon.world.scenegraph.VisualEntity
import com.gmail.caelum119.utils.collections.CallbackArrayList
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface

/**
 * First created 1/9/2017 in BalloonEngine
 */
abstract class BalloonRenderer {

    abstract var rootBalloonBalloonNode: BalloonNode
    abstract var rendererName: String
    abstract var events: ListenerInterface
    abstract var engineInstance: ClientEngine
    abstract var rootNode: BalloonNode

    var displayChunks = CallbackArrayList<CallbackCategorizingNSPImpl>({

    }, {

    })

    fun start() {
        //Add all chunks that already have Perspectives in them to displayChunks
        val chunksWithPerspectives = engineInstance.chunks.filter { it.getComponentsByType(Perspective::class).isNotEmpty() }
        displayChunks.addAll(chunksWithPerspectives)
        //Whenever a Perspective component is added to a chunk, add it to displayChunks
        engineInstance.chunks
                .filter { it is CallbackCategorizingNSPImpl }
                .forEach { it ->
                    it.callbacks.addListener { event: CallbackCategorizingNSPImpl.EventTypes.C_COMPONENT_ADDED ->
                        if (event.component is Perspective) {
                            val residingChunk = event.generalEntity.residingChunk
                            if (residingChunk !in displayChunks) {
                                displayChunks.add(residingChunk as CallbackCategorizingNSPImpl)
                            }
                        }
                    }
                }
        //Create the visualCounterpart of each physical entity in displayChunks
        for (displayChunk in displayChunks) {
            for (physicalEntity in displayChunk.physicalEntities) {
            }
        }
    }

    fun instantiateVisualCounterpart(physicalEntity: PhysicalEntity): VisualEntity {
        val vcclass = physicalEntity.visualCounterpart.getConstructor(PhysicalEntity::class.java, BalloonNode::class.java)
        return vcclass.newInstance(physicalEntity, physicalEntity)
    }

    open class EventTypes : EventCollection() {
        open class preInitRender(val balloonRenderer: BalloonRenderer) : EventType()
        open class postInitRender(val balloonRenderer: BalloonRenderer) : EventType()
    }
}