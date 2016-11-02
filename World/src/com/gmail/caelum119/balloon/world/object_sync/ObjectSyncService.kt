package com.gmail.caelum119.balloon.world.object_sync


import com.gmail.caelum119.utils.network.Connection
import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 9/27/2016 in BalloonEngine
 */
object ObjectSyncService {
    var server: Boolean = false
    val deltaClasses = HashMap<TransferableObject, HashMap<KProperty<*>, Any>>()
    val recipients = ArrayList<Connection>()
    private var temporaryIdsServed: Long = 0

    private fun updateRecipents() {
        recipients.forEach {
            it.sendTag(WorldSyncTag(deltaClasses))
        }
    }

    internal fun addDeltaProperty(containingClass: TransferableObject, propertyToSync: KProperty<*>, value: Any) {
        deltaClasses.putIfAbsent(containingClass, HashMap<KProperty<*>, Any>())
        deltaClasses[containingClass]?.put(propertyToSync, value)
    }

    fun acquireSyncableObjectId(): Long {
        return --temporaryIdsServed
    }
}