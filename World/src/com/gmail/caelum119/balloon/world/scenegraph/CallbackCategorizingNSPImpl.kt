package com.gmail.caelum119.balloon.world.scenegraph

import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.balloon.world.engine.components.VisualComponent
import com.gmail.caelum119.utils.collections.CallbackArrayList
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface
import java.io.Serializable
import java.util.*
import kotlin.reflect.KClass

/**
 * First created 1/5/2017 in BalloonEngine
 *
 * Implementation of [CategorizingNSP] that has callbacks.
 *
 * Whenever a entity or component is added, appropriate [callbacks] will be called.
 */
//CONSIDER: Instead of housing the event system in this class, simply expose the event system of individual
abstract class CallbackCategorizingNSPImpl : CategorizingNSP, Serializable{

    override val allEntities = newCallbackArrayList<GeneralEntity, EventTypes.E_ENTITY_ADDED, EventTypes.E_ENTITY_REMOVED>()
//    override val allComponents = newCallbackArrayList<Component, EventTypes.>() blah blah, useless code to be replaced
    val visualEntities = newCallbackArrayList<VisualEntity, EventTypes.E_VISUAL_ENTITY_ADDED, EventTypes.E_VISUAL_ENTITY_ADDED>()
    val physicalEntities = newCallbackArrayList<PhysicalEntity, EventTypes.E_PHYSICAL_ENTITY_ADDED, EventTypes
    .E_PHYSICAL_ENTITY_ADDED>()
    val components = newCallbackArrayList<Component, EventTypes.C_COMPONENT_ADDED, EventTypes.C_COMPONENT_REMOVED>()
    val physicalEntitiesByType = HashMap<Class<PhysicalEntity>, ArrayList<PhysicalEntity>>()
    val componentsByType = HashMap<Class<Component>, ArrayList<Component>>()
    val visualComponents = ArrayList<VisualComponent>()
    val visualComponentsByType = HashMap<Class<Component>, ArrayList<VisualComponent>>()

    override fun getAllVisualEntities(): ArrayList<VisualEntity> = visualEntities
    override fun getAllPhysicalEntities(): ArrayList<PhysicalEntity> = physicalEntities
    override fun getAllPhysicalEntitiesByType(): HashMap<Class<PhysicalEntity>, ArrayList<PhysicalEntity>> =
            physicalEntitiesByType

    override fun getAllComponentsByType(): HashMap<Class<Component>, ArrayList<Component>> = componentsByType
    override fun getAllVisualComponents(): ArrayList<VisualComponent> = visualComponents
    override fun getAllVisualComponentsByType(): HashMap<Class<Component>, ArrayList<VisualComponent>> = visualComponentsByType



    /**
     * Private utility function to easily set up the [CallbackArrayList]'s added abd removed callbacks to call
     * [eventTrigerer] ([callbacks]) callbacks.
     */
    inline private fun <E, reified addedET : EventType, reified removedET : EventType> newCallbackArrayList():
            CallbackArrayList<E> {
        return CallbackArrayList<E>({ addedElement ->
            eventTrigerer.triggerEvent(addedET::class.constructors.first().call(addedElement))
        }, { removedElement ->
            eventTrigerer.triggerEvent(removedET::class.constructors.first().call(removedElement))
        })
    }

    /**
     * Returns this as a physical entity, if it is one. Null otherwise.
     */
    override fun asPhysicalEntity(): PhysicalEntity? {
        return super.asPhysicalEntity()
    }

    override fun addChild(entityToAdd: GeneralEntity): Boolean {
        return super.addChild(entityToAdd)
    }

    override fun addComponent(component: Component) {
        super.addComponent(component)
        eventTrigerer.triggerEvent(EventTypes.C_COMPONENT_ADDED(component.attachedEntity, component))
    }

    /**
     * Returns an ArrayList of all components assigned to entities within this chunk of type [type].
     * If this chunk does not contain any entities with component of type [type], an empty ArrayList<[T]> is returned
     */
    override fun <T : Component> getComponentsByType(type: Class<T>): ArrayList<T> {
        return super.getComponentsByType(type)
    }

    /**
     * Returns an ArrayList of all components assigned to entities within this chunk of type [type].
     * If this chunk does not contain any entities with component of type [type], an empty ArrayList<[T]> is returned
     */
    override fun <T : Component> getComponentsByType(type: KClass<T>): ArrayList<T> {
        return super.getComponentsByType(type)
    }

    override var children = ArrayList<GeneralEntity>()

    /**** Events */

    open class EventTypes() : EventCollection() {
        /////Entity
        class E_ENTITY_ADDED(val generalEntity: GeneralEntity) : EventType()

        class E_ENTITY_REMOVED(val generalEntity: GeneralEntity) : EventType()

        class E_PHYSICAL_ENTITY_ADDED(val generalEntity: GeneralEntity) : EventType()
        class E_PHYSICAL_ENTITY_REMOVED(val generalEntity: GeneralEntity) : EventType()

        class E_VISUAL_ENTITY_ADDED(val generalEntity: GeneralEntity) : EventType()
        class E_VISUAL_ENTITY_REMOVED(val generalEntity: GeneralEntity) : EventType()
        /////Component
        class C_COMPONENT_ADDED(val generalEntity: GeneralEntity, val component: Component) : EventType()

        class C_COMPONENT_REMOVED(val generalEntity: GeneralEntity, val component: Component) : EventType()
    }

    protected open val eventTrigerer = EventTypes()
    val callbacks = ListenerInterface(eventTrigerer)

    //TODO
    override fun removeChild(child: GeneralEntity): Boolean {
        throw Exception("too-lazy-to-implement(TLTI) exception")
    }
}