package com.gmail.caelum119.balloon.world.scenegraph

import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface
import java.io.Serializable
import java.util.*

/**
 * Base entity class for allEntities that exist in 3d space, be them visual or physical.
 * For anything that exists in the game, with or without a physical location or representation.
 *
 * Entities may have Components and their associated Systems attached to them.
 */
//TODO: Remove smelly-ness regarding having the parent set it's subject's parent for it.
open class GeneralEntity(residingChunk: CategorizingNSP, vararg componentsToAdd: Component): Serializable {

    var residingChunk: CategorizingNSP = residingChunk
        set(newChunk) {
            eventTrigerer.triggerEvent(EventTypes.E_CHUNK_CHANGED(newChunk))
        }
    val subModels = ArrayList<GeneralEntity>()
    private val components = HashMap<Class<Component>, ArrayList<Component>>()
    val allComponents = ArrayList<Component>()

    init {
        componentsToAdd.forEach { addComponent(it) }
    }

    fun addComponent(componentToAdd: Component) {
        eventTrigerer.triggerEvent(EventTypes.E_COMPONENT_ADDED(componentToAdd))

        var componentListOfThatType: ArrayList<Component> = components[componentToAdd.javaClass] ?: ArrayList()
        componentListOfThatType.add(componentToAdd)
        allComponents.add(componentToAdd)
    }

    /**
     * Returns this as a physical entity, if it is one. Null otherwise.
     */
    fun asPhysicalEntity(): PhysicalEntity? {
        if (this is PhysicalEntity) {
            return this
        }
        return null
    }

    open class EventTypes() : EventCollection() {
        class E_COMPONENT_ADDED(val componentToAdd: Component) : EventType()
        class E_COMPONENT_REMOVED(val componentToRemove: Component) : EventType()
        class E_CHUNK_CHANGED(val newChunk: CategorizingNSP) : EventType()
    }

    open val eventTrigerer = EventTypes()
    open public val events = ListenerInterface(eventTrigerer)
}
