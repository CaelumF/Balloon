package com.gmail.caelum119.balloon.world.object_sync

import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * First created 9/27/2016 in BalloonEngine
 *
 */
interface NetworkSyncableProperty<T> {
    var value: T
    var lastModified: Long
    val informationGatherer: (T) -> TransmittablePropertySequence
    //Maybe not necessary
    val containingClass: TransferableObject

    operator fun getValue(thisRef: TransferableObject?, property: KProperty<*>): T {
        return value
    }// TODO: Maybe list the time values were accessed, so nodes can be recursively recalcuated with data received data.

    operator fun setValue(thisRef: TransferableObject, property: KProperty<*>, value: Any) {
        //Register $thisRef's $property as changed on ObjectSyncServer
        //Send value over to ObjectSyncServer with the key as the ref and then property, in case it changes multiple times per tick
        ObjectSyncService.addDeltaProperty(thisRef, property, value)

        thisRef.modifiedProperties[property] = value
    }

    fun add() {

    }
}

/**
 * Type is inferred from the return value of [initialValue]
 *
 * Properties will be acquired using slightly slow reflection, unless a informationGatherer can be acquired lazily for [initialValue]'s type.
 * TODO
 */
inline fun <reified T> networkSyncable(noinline initialValue: () -> T, containingClass: TransferableObject): NetworkSyncableProperty<T> {
//    val initialValueAfterInvokation = initialValue() // Don't evaluate the initial value twice.

    @Suppress("UNCHECKED_CAST")
    val informationGatherer: (T) -> TransmittablePropertySequence<*> =
            InformationGatherers.informationGathererMap.getOrDefault(T::class, InformationGatherers.defaultInformationGatherer) as ((T) -> TransmittablePropertySequence<*>)

    return ClientDomainImpl<T>(initialValue, informationGatherer, containingClass)
}

fun <T> networkSyncable(initialValue: () -> T, informationGatherer: (T) -> TransmittablePropertySequence<*>, containingClass: TransferableObject): NetworkSyncableProperty<T> {
    return ClientDomainImpl<T>(initialValue, informationGatherer, containingClass)

}

class ClientDomainImpl<T>(initialValue: () -> T,
                          override val informationGatherer: (T) -> TransmittablePropertySequence<*>,
                          override val containingClass: TransferableObject) : NetworkSyncableProperty<T> {

    override var lastModified: Long = System.currentTimeMillis()
    override var value: T = initialValue()
}

fun <T, T2> defineSyncableRuleset(t: QuicklySyncableObject<T, T2>.(T) -> Any) {
    t()
}

open class ImportantInfoHelper() {

}

open class QuicklySyncableObject<T : Any, T2>() {
    internal val properties = HashMap<String, KProperty<*>>()

    infix fun Any.fromMethod(t: Any) {

    }

    fun builder(builder: Builder<T>.(T2) -> T) {

    }

    fun setRetriever(builder: Retriever<T>.(TransmittablePropertySequence<Any>) -> Any): Retriever<T> {
        val retriever = Retriever<T>()
    }

    fun retriever2(classs: Class<*>) {

    }

    fun defineImportantFields() {

    }

    fun <PT> addProperty(property: KMutableProperty1<T, PT>) {
        properties.put(property.name, property)
    }
}

open class Builder<in T : Any>(val owningSyncableObject: QuicklySyncableObject<T, *>) {

    /**
     * Copies all of [owningSyncableObject]'s fields into [propertyReceiver]
     */
    fun reflectValuesOnto(propertyReceiver: T) {
        owningSyncableObject.properties.forEach { key, kProperty ->
            val property = propertyReceiver.javaClass.getField(key)
            property.set(owningSyncableObject, kProperty.javaField!!.get(propertyReceiver))
        }
    }

    fun test() {

    }
}

open class Retriever<T>(val it: T) {

}

val registeredClassUIDs = HashMap<Class<*>, Long>()
inline fun <reified T> getTransmittablePropertySequence(vararg properties: Any): TransmittablePropertySequence<T> {
    registeredClassUIDs.putIfAbsent(T::class.javaClass, registeredClassUIDs.size.toLong())
    return TransmittablePropertySequence<T>(registeredClassUIDs[T::class.javaClass] ?: 0, properties)
}

class TransmittablePropertySequence<T>(val classUID: Long, vararg properties: Any) {

}

class TransmittablePropertySequence1<T>(vararg properties: Any) {

}

class TransmittablePropertySequence2<T, T2>(vararg properties: Any) {

}