package com.gmail.caelum119.balloon.world.object_sync

import com.gmail.caelum119.utils.network.NetworkTag
import java.util.*
import kotlin.reflect.KProperty

/**
 * First created 10/2/2016 in BalloonEngine
 */
class WorldSyncTag(val propertiesToSync: HashMap<TransferableObject, HashMap<KProperty<*>, Any>>): NetworkTag() {

}