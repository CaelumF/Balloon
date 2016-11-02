package com.gmail.caelum119.balloon.world.scenegraph

import com.ardor3d.math.Vector3
import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.dynamics.RigidBody
import com.gmail.caelum119.balloon.world.engine.physics.physical_components.Ferrous
import com.gmail.caelum119.balloon.world.engine.physics.physical_components.Magnetic
import com.gmail.caelum119.balloon.world.object_sync.TransferableObjectImpl
import com.gmail.caelum119.balloon.world.object_sync.networkSyncable
import com.gmail.caelum119.engine.BulletPhysicsArea
import com.gmail.caelum119.scenegraph.ModelService
import com.gmail.caelum119.scenegraph.ModelService.SphereConstructParameters
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.vecmath.Vector3f

/**
 * First created 3/20/2016 in Engine
 * Link between the physical entity system and the higher level entity system.
 * Also provides an interface to interact with the physical world
 */
public class Chunk(val chunkX: Int, val chunkY: Int, val chunkZ: Int) : TransferableObjectImpl() {
    val JBulletPhysics by networkSyncable({ BulletPhysicsArea() }, this)
    val entities = ArrayList<GeneralEntity>()
    val onEntityCreateListeners = ArrayList<(GeneralEntity) -> Unit>()

    var eventQueue: Runnable? = null
    var TPS: Int = 0
    var DTPS: String = "0"

    init {

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(Runnable {
            DTPS = TPS.toString()
            TPS = 0
        }, 1, 1, TimeUnit.SECONDS)
    }

    fun tick() {
        entities.forEach { entity -> entity.physicalEntity?.onPhysicalInfoUpdate?.forEach { it.invoke(entity.physicalEntity as PhysicalEntity) } }

        JBulletPhysics.dynamicsWorld.stepSimulation(1 / 60f, 10)
        TPS++
        eventQueue?.run()
        eventQueue = null
    }

    fun createSphere(size: Float, translation: Vector3f) {
        eventQueue = (Runnable {
            val generalEntity: GeneralEntity = ModelService.sphereConstruct.getGeneralEntityCopy(SphereConstructParameters(1.0, 8, 100.0f), this)
            entities.add(generalEntity)
            generalEntity.addComponent(Magnetic(generalEntity, 0.2))
            generalEntity.addComponent(Ferrous(generalEntity))
            val rand = Random()
            generalEntity.physicalEntity?.applyForce(Vector3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()).multiplyLocal(6.0))

            JBulletPhysics.dynamicsWorld.addRigidBody(generalEntity.physicalEntity?.jbObject as RigidBody)
            (generalEntity.physicalEntity?.jbObject as RigidBody).translate(translation)
            onEntityCreateListeners.forEach {
                it.invoke(generalEntity)
            }
        })
    }

    fun addEntity(physicalEntity: PhysicalEntity): Unit {
        when (physicalEntity.jbObject) {
            is RigidBody -> {
                JBulletPhysics.dynamicsWorld.addRigidBody(physicalEntity.jbObject as RigidBody)
            }
            is CollisionObject -> {
                JBulletPhysics.dynamicsWorld.addCollisionObject(physicalEntity.jbObject)
            }
        }
    }
}
