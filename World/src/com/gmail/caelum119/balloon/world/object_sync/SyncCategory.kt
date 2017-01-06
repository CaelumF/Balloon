package com.gmail.caelum119.balloon.world.object_sync

import java.util.*

/**
 * First created 11/6/2016 in BalloonEngine
 *
 * SyncCategories categorise and store syncable objects and provide hashes of their content to minimise the effort to transmit and store syncable object data
 *
 * All SyncableObjects belong to a category.
 * A SyncableObject's hash is calculated based on its [containedSyncableObjects] (which could contain child categories).
 *
 */
class SyncCategory<T>(var parentSyncCategory: SyncCategory<*>? = null) {
    var containedSyncableObjects = ArrayList<T>()

    init {
        defineSyncMethod<SyncCategory<*>, TransmittablePropertySequence> { saidObject ->
            addProperties(SyncCategory<*>::containedSyncableObjects)

            importantPropertyRetriever{
                getTPSFromProperties(saidObject)
            }

            instanceBuilder {
                val propertyReceiver = SyncCategory<Any>()
                reflectValuesOnto(propertyReceiver)
                return@instanceBuilder propertyReceiver
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SyncCategory<*>

        if (parentSyncCategory != other.parentSyncCategory) return false
        if (containedSyncableObjects != other.containedSyncableObjects) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parentSyncCategory?.hashCode() ?: 0
        result = 31 * result + containedSyncableObjects.hashCode()
        return result
    }
}