package com.gmail.caelum119.balloon.world.object_sync


import com.gmail.caelum119.balloon.world.object_sync.deprecated.TransferableObject
import com.gmail.caelum119.utils.network.Connection
import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 9/27/2016 in BalloonEngine
 * Stores information(
 */
object ObjectSyncService {
    /**
     * Maybe deprecated
     */
    var server: Boolean = false

    /**
     * Delta detection
     */
    val deltaClasses = HashMap<TransferableObject, HashMap<KProperty<*>, Any>>()
    /**
     * Distribution
     */
    val recipients = ArrayList<Connection>()
    val syncableObjects = HashMap<SyncCategory<*>, ArrayList<Any>>()
    private var temporaryIdsServed: Long = 0

    private fun updateRecipents() {
        recipients.forEach {
//            it.sendTag(WorldSyncTag(deltaClasses))
        }
    }

    internal fun addDeltaProperty(containingClass: TransferableObject, propertyToSync: KProperty<*>, value: Any) {
        deltaClasses.putIfAbsent(containingClass, HashMap<KProperty<*>, Any>())
        deltaClasses[containingClass]?.put(propertyToSync, value)
    }

    fun acquireSyncableObjectId(): Long {
        return --temporaryIdsServed
    }

    fun addSyncableObject(category: SyncCategory<*>, instance: Any) {
        syncableObjects.getOrPut(category, {ArrayList<Any>()}).add(instance)
    }
}