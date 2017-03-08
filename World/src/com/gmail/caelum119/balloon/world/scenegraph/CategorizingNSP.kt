package com.gmail.caelum119.balloon.world.scenegraph

import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.balloon.world.engine.components.VisualComponent
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.ListenerInterface
import java.util.*
import kotlin.reflect.KClass

/**
 * First created 12/30/2016 in BalloonEngine
 *
 * Categorising Non-intermittent Spatial Partition.
 * Stores entities and components in a categorised manner to minimise iterations.
 * TODO: Add more categories.
 */
interface CategorizingNSP : NonintermittentSpatialPartition<Any, ArrayList<GeneralEntity>> {


    fun getAllVisualEntities(): ArrayList<VisualEntity>
    fun getAllPhysicalEntities(): ArrayList<PhysicalEntity>
    fun getAllPhysicalEntitiesByType(): HashMap<Class<PhysicalEntity>, ArrayList<PhysicalEntity>>
    fun getAllComponentsByType(): HashMap<Class<Component>, ArrayList<Component>>
    fun getAllVisualComponents(): ArrayList<VisualComponent>
    fun getAllVisualComponentsByType(): HashMap<Class<Component>, ArrayList<VisualComponent>>

    val events: ListenerInterface
    val eventTriggerer: EventCollection

    /**
     * Returns this as a physical entity, if it is one. Null otherwise.
     */
    fun asPhysicalEntity(): PhysicalEntity? {
        if (this is PhysicalEntity) {
            return this
        }
        return null
    }


    override fun addChild(entityToAdd: GeneralEntity): Boolean {
        entityToAdd.residingChunk = this
        allEntities.add(entityToAdd)

        if (entityToAdd is VisualEntity) {
            getAllVisualEntities().add(entityToAdd)
        }

        if (entityToAdd is PhysicalEntity) {
            getAllPhysicalEntities().add(entityToAdd)
            getAllPhysicalEntitiesByType().computeIfAbsent(entityToAdd.javaClass, { ArrayList() }).add(entityToAdd)
        }
        //Transfer all components to this chunk.
        entityToAdd.allComponents.forEach { addComponent(it) }
        return true
    }

    fun addComponent(component: Component) {
        val componentClass: Class<Component> = component.javaClass
        allComponents.add(component)
        getAllComponentsByType().computeIfAbsent(component.javaClass, { ArrayList<Component>() }).add(component)

        if (component is VisualComponent) {
            getAllVisualComponents().add(component)
            getAllVisualComponentsByType().computeIfAbsent(componentClass, { ArrayList() }).add(component)
        }

        if (component is PhysicalEntity) {

        }
    }

    /**
     * Returns an ArrayList of all components assigned to entities within this chunk of type [type].
     * If this chunk does not contain any entities with component of type [type], an empty ArrayList<[T]> is returned
     */
    fun <T : Component> getComponentsByType(type: Class<T>): ArrayList<T> {
        val listByThatType: ArrayList<T>? = getAllComponentsByType()[type as Class<Component>] as ArrayList<T>
        return listByThatType ?: ArrayList<T>()
    }

    /**
     * Returns an ArrayList of all components assigned to entities within this chunk of type [type].
     * If this chunk does not contain any entities with component of type [type], an empty ArrayList<[T]> is returned
     */
    fun <T : Component> getComponentsByType(type: KClass<T>): ArrayList<T> {
        val listByThatType: ArrayList<T>? = getAllComponentsByType()[type as Class<Component>] as ArrayList<T>
        return listByThatType ?: ArrayList<T>()
    }
}