package com.gmail.caelum119.balloon.world.object_sync.deprecated

import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 9/27/2016 in BalloonEngine
 * TODO: Interface to add to class so that it can be efficiently scanned for changed propertyList.
 */
@Deprecated("Kotlin-friedly object syncing in the works at NetworkSyncalbeProperty.kt")
open class TransferableObjectImpl(): TransferableObject {
    /**
     * To differentiate this Syncable object from others.
     * Objects with the same TransferableObjectId will be synced across the network.
     *
     * When a client creates an object, it will be assigned a temporary, negative NSID while it gets a proper NSID from the server.
     */
    override var TransferableObjectId: Long = 4/*ObjectSyncService.acquireSyncableObjectId()*/

    /**
     * List of modified propertyList.
     */
    override val modifiedProperties = HashMap<KProperty<*>, Any>()

}