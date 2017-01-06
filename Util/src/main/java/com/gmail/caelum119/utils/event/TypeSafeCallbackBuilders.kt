package com.gmail.caelum119.utils.event

import java.util.*

/**
 * First created 1/1/2017 in BalloonEngine
 */

class Callback<in ET : EventType>(val method: (ET) -> Unit)

/**
 * Interface to allow publicly adding listeners to a EventCollection instance without exposing the EventCollection
 * itself.
 */
class ListenerInterface(private val eventCollection: EventCollection) {
    fun <ET : EventType> addListener(eventType: Class<ET>, callback: Callback<ET>) {
        eventCollection.addListener(eventType, callback)
    }

    inline fun <reified T : EventType> addListener(noinline callback: (T) -> Unit) {
        addListener(T::class.java, Callback(callback))
    }
}

abstract class EventType() {

}

/**
 *
 */
open class EventCollection() {
    val listeners = HashMap<Class<*>, ArrayList<Callback<*>>>()

    inline fun <reified ET : EventType> triggerEvent(eventInstance: ET) {
        listeners[ET::class.java]?.forEach {
            (it as Callback<ET>).method(eventInstance)
        }
    }

    fun <ET : EventType> addListener(eventType: Class<ET>, callback: Callback<ET>) {
        listeners.computeIfAbsent(eventType, { ArrayList<Callback<*>>() }).add(callback)
    }
}