package com.gmail.caelum119.balloon.world.object_sync

import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 9/27/2016 in BalloonEngine
 * TODO: Interface to add to class so that it can be efficiently scanned for changed properties.
 */
open class TransferableObjectImpl(): TransferableObject {
    /**
     * To differentiate this Syncable object from others.
     * Objects with the same TransferableObjectId will be synced accross the network.
     *
     * When a client creates an object, it will be assigned a temporary, negative NSID while it gets a proper NSID from the server.
     */
    override var TransferableObjectId: Long = 4/*ObjectSyncService.acquireSyncableObjectId()*/

    /**
     * List of modified properties.
     */
    override val modifiedProperties = HashMap<KProperty<*>, Any>()

}