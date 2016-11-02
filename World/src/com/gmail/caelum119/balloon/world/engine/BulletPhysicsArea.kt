package com.gmail.caelum119.engine

import com.bulletphysics.collision.broadphase.DbvtBroadphase
import com.bulletphysics.collision.dispatch.CollisionDispatcher
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration
import com.bulletphysics.collision.shapes.CollisionShape
import com.bulletphysics.collision.shapes.SphereShape
import com.bulletphysics.collision.shapes.StaticPlaneShape
import com.bulletphysics.dynamics.DiscreteDynamicsWorld
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.dynamics.RigidBodyConstructionInfo
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver
import com.bulletphysics.linearmath.DefaultMotionState
import javax.vecmath.Vector3f

/**
 * First created 5/16/2016 in Engine
 */
class BulletPhysicsArea(){
    val dynamicsWorld: DiscreteDynamicsWorld;
    val fallRigidBodyCI: RigidBodyConstructionInfo

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
    }
}