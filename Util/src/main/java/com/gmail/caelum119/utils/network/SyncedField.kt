package com.gmail.caelum119.utils.network

import kotlin.reflect.KClass

/**
 *
 */
@Deprecated("Working on a different implementation")
@Retention(AnnotationRetention.RUNTIME)
public annotation class SyncedField(val updateClashPolicy: KClass<Any>)

public enum class Domain() {
    CLIENT, SERVER, ENGINE
}

public interface UpdateClashPolicy<T, T2> {
    /**
     *
     */
    public fun resolveClash(localData: T, otherData: T2)
}