package com.gmail.caelum119.balloon.world.scenegraph

import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.dynamics.RigidBody
import com.gmail.caelum119.balloon.world.engine.BulletPhysicsArea
import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.ListenerInterface
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * First created 3/20/2016 in Engine
 * Link between the physical entity system and the higher level entity system.
 * Also provides an interface to interact with the physical world.
 *
 * Chunks
 */
class Chunk(@Transient val residingInstance: EngineInstance, val chunkX: Int, val chunkY: Int, val chunkZ: Int,
            override var parent: Any) : CallbackCategorizingNSPImpl(){
    override fun getNorth(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEast(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWest(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSouth(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAbove(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBellow(): SpatialPartition<*, *, *> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


//    override val physicsArea: BulletPhysicsArea

    override val allComponents = ArrayList<Component>()

    override val physicsArea = BulletPhysicsArea(this)

    //TODO: Change ArrayList to some List implementation better suited for element removal.

    val onEntityCreateListeners = ArrayList<(GeneralEntity) -> Unit>()
    var eventQueue: Runnable? = null
    var TPS: Int = 0
    var DTPS: String = "0"

    init {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            DTPS = TPS.toString()
            TPS = 0
        }, 1, 1, TimeUnit.SECONDS)
    }

    fun tick() {
        //Invoke all of this chunk's physical entities's physical tick methods.
        getAllPhysicalEntities().forEach { pEntity -> pEntity.onPhysicalInfoUpdate.forEach { it.invoke(pEntity) } }

        physicsArea.dynamicsWorld.stepSimulation(1 / 60f, 10)
        TPS++
        eventQueue?.run()
        eventQueue = null
    }

//    fun createSphere(size: Float, translation: Vector3f) {
//        eventQueue = (Runnable {
////            val newPhysicalEntity: GeneralEntity = ModelService.sphereConstruct.getGeneralEntityCopy(SphereConstructParameters(1.0, 8, 100.0f), this)
//            allEntities.add(newPhysicalEntity)
//            newPhysicalEntity.addComponent(Magnetic(newPhysicalEntity as PhysicalEntity, 0.2))
//            newPhysicalEntity.addComponent(Ferrous(newPhysicalEntity))
//            val rand = Random()
////            newPhysicalEntity.applyForce(Vector3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).multiplyLocal(6.0))
//
//            physicsArea.dynamicsWorld.addRigidBody(newPhysicalEntity.jbObject as RigidBody)
//            newPhysicalEntity.jbObject.translate(translation)
//
//            onEntityCreateListeners.forEach { entityCreationListener ->
//                entityCreationListener.invoke(newPhysicalEntity)
//            }
//        })
//    }

    override fun addChild(entityToAdd: GeneralEntity): Boolean {
        super.addChild(entityToAdd)

        //Add entity to bullet instance
        if (entityToAdd is PhysicalEntity) {
            //Add it to the dynamic world using the appropriate method
            when (entityToAdd.jbObject) {
                is RigidBody -> {
                    physicsArea.dynamicsWorld.addRigidBody(entityToAdd.jbObject as RigidBody)
                }
                is CollisionObject -> {
                    physicsArea.dynamicsWorld.addCollisionObject(entityToAdd.jbObject as CollisionObject)
                }
            }
        }
        return true
    }

    override val eventTriggerer = EventCollection()
    override val events = ListenerInterface(eventTriggerer)
}
