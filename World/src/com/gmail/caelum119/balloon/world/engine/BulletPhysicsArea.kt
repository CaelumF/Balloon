package com.gmail.caelum119.balloon.world.engine

import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.SphereShape
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.*
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import com.bulletphysics.linearmath.DefaultMotionState
import com.gmail.caelum119.balloon.world.scenegraph.NonintermittentSpatialPartition
import com.gmail.caelum119.balloon.world.scenegraph.PhysicalEntity
import com.gmail.caelum119.utils.event.EventCollection
import java.util.*
import javax.vecmath.Vector3f

/**
 * First created 5/16/2016 in Engine
 */
class BulletPhysicsArea(residingNonintermittentSpatialPartition: NonintermittentSpatialPartition<*, *>) {
    val dynamicsWorld: DiscreteDynamicsWorld
    val fallRigidBodyCI: RigidBodyConstructionInfo
    val subtickCallback = ArrayList<(BulletPhysicsArea) -> Unit>()
    val residingNISP = residingNonintermittentSpatialPartition

    init {
        val broadPhase = DbvtBroadphase()

        val configuration = DefaultCollisionConfiguration()
        val dispatcher = CollisionDispatcher(configuration)

        val solver = SequentialImpulseConstraintSolver()

        dynamicsWorld = DiscreteDynamicsWorld(dispatcher, broadPhase, solver, configuration)

        dynamicsWorld.setGravity(Vector3f(0f, 0f, 0f))
        val groundShape: CollisionShape = StaticPlaneShape(Vector3f(0f, 2f, 0f), 10f)
        val groundMotionState = DefaultMotionState()
        val groundRigidBodyCI = RigidBodyConstructionInfo(0f, groundMotionState, groundShape, Vector3f(0f, 0f, 0f))
//        groundRigidBodyCI.friction = 1.0f
        val groundRigidBody = RigidBody(groundRigidBodyCI)

//        dynamicsWorld.addRigidBody(groundRigidBody)

        val fallShape: CollisionShape = SphereShape(1f)
//        val fallMotionState = DefaultMotionState(Transform()/*Transform(Matrix3f(30f, 50f, 40f, 30f, 20f, 1f, 10f, 5f, 60f))*/)
        val fallInertia = Vector3f(0f, 1f, 0f)
        fallShape.calculateLocalInertia(1f, fallInertia)
        fallRigidBodyCI = RigidBodyConstructionInfo(10f, null, fallShape, fallInertia)
        fallRigidBodyCI.friction = 0.5f
//        fallRigidBodyCI.angularDamping = 0.01f
//        fallRigidBodyCI.linearDamping = 0.1f
        fallRigidBodyCI.restitution = 0.4f

        //Ran every subtick
        dynamicsWorld.setInternalTickCallback(object : InternalTickCallback() {
            override fun internalTick(world: DynamicsWorld, timeStep: Float) {
                //Call all subtick callbacks
                subtickCallback.forEach {
                    it.invoke(this@BulletPhysicsArea)
                }

                //Find all collisions and report them to involved colliders
                for (i in 0..dispatcher.numManifolds) {
                    val curiManifold = dispatcher.getManifoldByIndexInternal(i)
                    val rigidBody0 = curiManifold.body0 as RigidBody // First colliding body
                    val rigidBody1 = curiManifold.body1 as RigidBody // Second colliding body
                    val physicalEntity0 = rigidBody0.userPointer as PhysicalEntity // First colliding physicalEntity
                    val physicalEntity1 = rigidBody1.userPointer as PhysicalEntity // Second colliding physicalEntity

                    physicalEntity0.onCollide(physicalEntity1, true)
                    physicalEntity1.onCollide(physicalEntity0, false)
                }
            }
        }, this)
    }

    class Events() : EventCollection() {

    }
}