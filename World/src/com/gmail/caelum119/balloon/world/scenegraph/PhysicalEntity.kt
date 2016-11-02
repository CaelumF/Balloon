package com.gmail.caelum119.balloon.world.scenegraph

import com.ardor3d.bounding.BoundingBox
import com.ardor3d.image.Texture
import com.ardor3d.image.TextureStoreFormat
import com.ardor3d.math.Vector3
import com.ardor3d.renderer.state.TextureState
import com.ardor3d.scenegraph.shape.Sphere
import com.ardor3d.util.TextureManager
import com.bulletphysics.collision.dispatch.CollisionObject
import com.bulletphysics.dynamics.RigidBody
import com.bulletphysics.linearmath.Transform
import com.gmail.caelum119.balloon.world.object_sync.TransferableObjectImpl
import com.gmail.caelum119.balloon.world.object_sync.networkSyncable
import java.util.*
import javax.vecmath.Quat4f
import javax.vecmath.Tuple3f
import javax.vecmath.Vector3f

/**;
 * First created 5/16/2016 in Engine
 * Wrapper for JBullet object, with physical geometries, properties etc.
 */
open class PhysicalEntity(jbObject: CollisionObject) : TransferableObjectImpl() {

    val jbObject: CollisionObject by networkSyncable({ jbObject }, this)

    var partOf: GeneralEntity? = null
    val onPhysicalInfoUpdate = ArrayList<(updating: PhysicalEntity) -> Unit>()

    init {
        val ts = TextureState()
        ts.isEnabled = true
        ts.texture = TextureManager.load("images/ardor3d_white_256.jpg", Texture.MinificationFilter.Trilinear,
                TextureStoreFormat.GuessCompressedFormat, true)

        val testSphere = Sphere("Sphere", 32, 32, 1.0)
        testSphere!!.modelBound = BoundingBox()
        testSphere!!.setRenderState(ts)
    }

    /**
     * Returns the JBullet transform of [jbObject]
     * Not thread safe. For thread safety, use jbObject.getTransform(new Transform())
     */
    fun getTransform(): Transform {
        return jbObject.getWorldTransform(Transform())
    }

    fun getLocationVector(): Vector3 {
        val XYZ = this.getTransform().locationToXYZDouble()
        return Vector3(XYZ.x, XYZ.y, XYZ.z)
    }

    fun getInterpolatedWorldTransform(): Transform {

        return jbObject.getInterpolationWorldTransform(Transform())
    }

    fun applyForce(force: Vector3) {
        val rigidBody = jbObject as RigidBody
        val preLinearVelocity = rigidBody.getLinearVelocity(Vector3f())
        preLinearVelocity.add(force.toTuple3f())

        rigidBody.setLinearVelocity(preLinearVelocity)
    }
}

fun Transform.locationToXYZDouble(): XYZDouble {
    return XYZDouble(this.origin.x.toDouble(), this.origin.y.toDouble(), this.origin.z.toDouble())
}

fun Quat4f.rotationToXYZWDouble(): XYZWDouble {
    return XYZWDouble(this.x.toDouble(), this.y.toDouble(), this.z.toDouble(), this.w.toDouble())
}

fun Vector3.toTuple3f(): Tuple3f {
    return Vector3f(this.xf, this.yf, this.zf)
}

data class XYZDouble(var x: Double, var y: Double, var z: Double)
data class XYZWDouble(var x: Double, var y: Double, var z: Double, var w: Double)