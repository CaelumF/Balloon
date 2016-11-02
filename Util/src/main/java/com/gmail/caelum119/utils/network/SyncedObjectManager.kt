package com.gmail.caelum119.utils.network

import sun.plugin.dom.exception.InvalidStateException
import java.util.*

/**
 * First created 2/28/2016 in Engine
 *
 * Objects from this side will have a positive index, from the other side will have a negative.
 */
@Deprecated("Working on  a different implementation")
object SyncedObjectManager {
    private val ourObjects = HashMap<Int, SyncedObjectPassport>()
    private val theirObjects = HashMap<Int, SyncedObjectPassport>()
    private val inited = true

    public fun addSyncedObject(toAdd: SyncedObjectPassport): Int {
        if (inited) {
            ourObjects[ourObjects.size] = toAdd
            return ourObjects.size
        }
        throw InvalidStateException("SyncedObjectManager not initialized. Call init(connection: Connection)")
    }

    public fun init(connection: Connection) {
        //Register with the other end to
        connection.addListener(SyncedPropertyCreateTag::class.java, fun(syncedObjectPassport: SyncedObjectPassport) {

        })
    }

    public fun create(otherEndHasAuthority: Boolean): SyncedObjectPassport {
        return SyncedObjectPassport(otherEndHasAuthority)
    }

    /**
     * First created 2/27/2016 in Engine
     * To be extended by generated classes, and maybe other things.
     */
    public class SyncedObjectPassport internal constructor(val otherEndHasAuthority: Boolean = false) {
        public val syncChannel: Int = 0
        public val ID: Int = 0

        public fun sync() {

        }

        fun Double.assign() {

        }
    }
}