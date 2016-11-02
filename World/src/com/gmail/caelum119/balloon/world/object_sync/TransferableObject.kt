package com.gmail.caelum119.balloon.world.object_sync

import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 10/9/2016 in BalloonEngine
 */
interface TransferableObject {
    /**
     * To differentiate this Syncable object from others.
     * Objects with the same TransferableObjectId will be synced accross the network.
     *
     * When a client creates an object, it will be assigned a temporary, negative NSID while it gets a proper NSID from the server.
     */
    var TransferableObjectId: Long

    /**
     * List of modified properties.
     */
    val modifiedProperties: HashMap<KProperty<*>, Any>

    fun addModifiedProperty(modifiedProperty: KProperty<*>, newValue: Any){
        modifiedProperties[modifiedProperty] = newValue
    }
}