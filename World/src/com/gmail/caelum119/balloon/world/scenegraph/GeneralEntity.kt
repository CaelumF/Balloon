package com.gmail.caelum119.balloon.world.scenegraph
import com.gmail.caelum119.balloon.world.object_sync.TransferableObjectImpl
import com.gmail.caelum119.scenegraph.Component
import java.util.*

/**
 * Container class with PhysicalEnitty and VisualEntity.
 */
//TODO: Remove smelly-ness regarding having the parent set it's subject's parent for it.
open class GeneralEntity(val residingChunk: Chunk): TransferableObjectImpl() {

    var physicalEntity: PhysicalEntity? = null
        set(value) {
            //When physicalEntity is set, register each component's physicalTick() to be called appropriately.
            physicalEntity?.onPhysicalInfoUpdate?.add {
                allComponents.forEach { it.physicsTick() }
            }
            field = value
        }
    var visualEntity: VisualEntity? = null
        set(value) {
            //When visualEntity is set, register each component's visualTick() to be called appropriately.
            visualEntity?.onVisualInfoUpdate?.add {
                allComponents.forEach { it.visualTick() }
            }
            field = value
        }

    val subModels = ArrayList<GeneralEntity>()
    private val components = HashMap<Class<Component>, ArrayList<Component>>()
    val allComponents = ArrayList<Component>()

    fun addComponent(componentToAdd: Component) {
        var componentListOfThatType: ArrayList<Component> = components[componentToAdd.javaClass]?: ArrayList()
        componentListOfThatType.add(componentToAdd)
        allComponents.add(componentToAdd)
    }

    init {
        physicalEntity?.partOf = this
        visualEntity?.partOf = this
    }
}
