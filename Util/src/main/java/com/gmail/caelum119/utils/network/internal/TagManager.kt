package com.gmail.caelum119.utils.network.internal

import com.gmail.caelum119.utils.network.NetworkTagOutdated
import java.util.*

/**
 *
 */
@Deprecated("Working on  a different implementation")
private var registeredTags: HashMap<Int, Class<out NetworkTagOutdated>> = HashMap()

public fun get(key: Int) = registeredTags[key]

public fun register(key: Int, value: Class<out NetworkTagOutdated>): Boolean {
    val d = registeredTags.get(key)
    d?.let {
        throw IllegalStateException("Already exists")
        return false
    }

    registeredTags.put(key, value)
    return true
}

