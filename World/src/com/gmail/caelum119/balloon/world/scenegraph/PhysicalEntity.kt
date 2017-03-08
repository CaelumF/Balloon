package com.gmail.caelum119.balloon.world.scenegraph

import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.linearmath.Transform
import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface
import java.util.*
import javax.vecmath.Tuple3f
import javax.vecmath.Vector3f

/**;
 * First created 5/16/2016 in Engine
 * Wrapper for JBullet object, with physical geometries, propertyList etc.
 */
abstract class PhysicalEntity(residingChunk: Chunk, vararg componentsToAdd: Component) :
        GeneralEntity(residingChunk, *componentsToAdd) {

    abstract val visualCounterpart: Class<VisualEntity>
    val jbObject: CollisionObject = CollisionObject()
    val onPhysicalInfoUpdate = ArrayList<(updating: PhysicalEntity) -> Unit>()

    init {
        jbObject.userPointer = this
    }

    /**
     * Returns the JBullet transform of [jbObject]
     * Not thread safe. For thread safety, use jbObject.getTransform(new Transform())
     */
    fun getTransform(): Transform {
        return jbObject.getWorldTransform(Transform())
    }

    fun getInterpolatedWorldTransform(): Transform {
        return jbObject.getInterpolationWorldTransform(Transform())
    }

    fun applyForce(force: Vector3f) {
        val rigidBody = jbObject as RigidBody
        val preLinearVelocity = rigidBody.getLinearVelocity(Vector3f())
        preLinearVelocity.add(force)
        rigidBody.setLinearVelocity(preLinearVelocity)
    }

    open class PhysicEvents(): GeneralEntity.EventTypes(){
        /**
         * Called whenever this physical entity collides with another.
         * [firstInformed] is true if this event was triggered before [otherPE] was informed of the collision.
         */
        class E_COLLISION_EVENT(val otherPE: PhysicalEntity, val firstInformed: Boolean): EventType()
    }

    open fun onCollide(involvedPhysicalEntity: PhysicalEntity, firstInformed: Boolean){
        eventTrigerer.triggerEvent(PhysicEvents.E_COLLISION_EVENT(involvedPhysicalEntity, firstInformed))
    }

    override val eventTrigerer = EventTypes()
    override val events = ListenerInterface(eventTrigerer)
}

fun Transform.getLocationFloatTriple() = Triple(this.origin.x, this.origin.y, this.origin.z)


fun Tuple3f.toVector3f(): Vector3f {
    return Vector3f(this.x, this.y, this.z)
}