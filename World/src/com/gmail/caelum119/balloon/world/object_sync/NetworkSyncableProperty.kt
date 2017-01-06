package com.gmail.caelum119.balloon.world.object_sync

import com.gmail.caelum119.balloon.world.object_sync.deprecated.TransferableObject
import java.io.Serializable
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField

/**
 * TODO: The delegation method of syncing propertyList
 */

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
//inline fun <reified T> networkSyncable(noinline initialValue: () -> T, containingClass: TransferableObject): NetworkSyncableProperty<T> {
////    val initialValueAfterInvokation = initialValue() // Don't evaluate the initial value twice.
//
////    @Suppress("UNCHECKED_CAST")
////    val informationGatherer: (T) -> TransmittablePropertySequence =
////            DefaultInformationGatherers.informationGathererMap.getOrDefault(T::class, InformationG) as ((T) -> TransmittablePropertySequence)
//
//    return ClientDomainImpl<T>(initialValue, informationGatherer, containingClass)
//}

//fun <T> networkSyncable(initialValue: () -> T, informationGatherer: (T) -> TransmittablePropertySequence<*>, containingClass: TransferableObject): NetworkSyncableProperty<T> {
//    return ClientDomainImpl<T>(initialValue, informationGatherer, containingClass)
//}

class ClientDomainImpl<T>(initialValue: () -> T,
                          override val informationGatherer: (T) -> TransmittablePropertySequence,
                          override val containingClass: TransferableObject) : NetworkSyncableProperty<T> {

    override var lastModified: Long = System.currentTimeMillis()
    override var value: T = initialValue()
}

/**
 * Lazily(does nothing if already declared for [T]) declaration existsDefines how instances of a class are synced.
 * [syncMethods]: The methodology used create instances from builders and builders from instances.
 *  TODO: Create non-lazy version.(It would be funny if I never get round to this)
 */
inline fun <reified T : Any, T2 : TransmittablePropertySequence> defineSyncMethod(noinline syncMethods: ImportantClassProtocol<T, T2>.(T) -> Any) {
    importantClassProtocols[T::class.java] = syncMethods as ImportantClassProtocol<Any, TransmittablePropertySequence>
}


val importantClassProtocols = HashMap<Class<*>, ImportantClassProtocol<Any, TransmittablePropertySequence>>()

/**
 * Defines protocols for encoding and decoding an object in a type-safe builder manner. Mostly so that special
 * serialization and deserialization business code can be simply and concisely stored inside the classes they
 * affect.
 */
class ImportantClassProtocol<T : Any, T2 : TransmittablePropertySequence>() {
    internal val importantProperties = HashMap<String, KProperty<*>>()
    /**
     * The retriver's job is to return a [TransmittablePropertySequence] with the passed [T]
     *
     */
    var importantPropertyRetriever: (Retriever<T>.(T) -> TransmittablePropertySequence)? = null
    var builder: (Builder<T, T2>.(T2) -> T)? = null


    /**
     * Type safe builder. The [importantPropertyRetriever] lambda parameter will be called with an instance of [T]
     * and should return a TransmittalbePropertySequence representing that instance.
     */
    fun importantPropertyRetriever(importantPropertyRetriever: Retriever<T>.(T) -> TransmittablePropertySequence) {
        this.importantPropertyRetriever = importantPropertyRetriever
    }

    /**
     * [builder] should construct an instance of [T]
     */
    fun instanceBuilder(builder: Builder<T, T2>.(T2) -> T) {
        this.builder = builder
    }

    fun <T : Any> getTPSFromProperties(instance: T): TransmittablePropertySequence {
        //Add each important field to an ArrayList
        val propertyValueList = ArrayList<TransmittablePropertyValue>()
        importantProperties.forEach { name, kProperty ->
            propertyValueList.add(TransmittablePropertyValue(kProperty.javaField!!.get(instance)))
        }

        return TransmittablePropertySequence(4, *propertyValueList.toTypedArray())
    }

    /**
     * Defines a property as important for automatic retrieval during property scraping, and
     * to be reflected onto instances when a new [T] is constructed.
     * @param PT: Property type. The type of property to be added.
     * @param property: Mutable property to be read from/set
     */
    fun addProperty(property: KMutableProperty1<*, *>) {
        importantProperties.put(property.name, property)
    }

    fun addProperties(properties: List<KMutableProperty1<*, *>>) {
        properties.forEach { addProperty(it) }
    }

    fun addProperties(vararg properties: KMutableProperty1<*, *>) {
        properties.forEach { addProperty(it) }
    }
}

open class Builder<T : Any, T2 : TransmittablePropertySequence>(val correspondingProtocol: ImportantClassProtocol<T, T2>, val dataSource: T2) {

    /**
     * Copies all of [dataSource]'s fields into [propertyReceiver] using [correspondingProtocol]
     */
    inline fun <reified T> reflectValuesOnto(propertyReceiver: T) {

        for (receiverField in T::class.javaClass.fields) {

            val classUID: Int = registeredClassUIDs[T::class.javaClass]?.toInt()
                    ?: throw Exception("Class ${T::class.javaClass.name} not registered")
            val fieldUID: Int = registeredFieldUIDs[Pair(classUID, receiverField.name)]
                    ?: throw Exception("Field ${receiverField.name} of ${T::class.javaClass.name} is not registered.")

            //Set propertyReceiver's receiverField to dataSource's associated value.
            dataSource.propertyMap[fieldUID]?.let { receiverField.set(propertyReceiver, it.value) }
        }
    }

    fun invokeBuilder() {
        correspondingProtocol.builder?.invoke(this, dataSource)
    }
}

open class Retriever<T>(val it: T) {

}

val registeredClassUIDs = HashMap<Class<*>, Long>()
//To remember field names by their
val registeredFieldUIDs = HashMap<Pair<Int, String>, Int>()

inline fun <reified T> getTransmittablePropertySequence(vararg properties: Any): TransmittablePropertySequence {
    registeredClassUIDs.putIfAbsent(T::class.javaClass, registeredClassUIDs.size.toLong())
    val transmittableProperties = ArrayList<TransmittablePropertyValue>()
    properties.forEach { transmittableProperties.add(TransmittablePropertyValue(it)) }
    return TransmittablePropertySequence(registeredClassUIDs[T::class.javaClass] ?: 0, *transmittableProperties.toTypedArray())
}

interface PropertyStorer {

}

class TransmittablePropertyValue(val value: Any): Serializable {
    /**
     * Field ID for reflecting onto objects.
     * TODO: Automatically generate, and agree across associated Engines.
     */
    val id: Int = 0

}

/**
 * A sequence of TransmittableProperties. It will be converted to a Byte array using Java Serialization,
 * transmitted across a network or stored in non-volatile memory and then re-loaded by another engine instance
 * to 'load' caches, saves and keep multiplayer games in sync.
 */
open class TransmittablePropertySequence(val classUID: Long, vararg properties: TransmittablePropertyValue)
:Serializable {
    val propertyList: Array<out TransmittablePropertyValue>
    val propertyMap = HashMap<Int, TransmittablePropertyValue>()

    init {
        this.propertyList = properties
        propertyList.forEach { propertyMap[it.id] = it }
    }

    /**
     * TODO: Function to return a TransmittablePropertySequence with this appended to it.
     * Also, probably with a name that's more explicit how it's merged.
     */
    fun mergeWith(otherTPS: TransmittablePropertySequence) {

    }

}

/**
 * Returns the data [of]'s classes' InformationGatherer returns.
 *
 * TODO: Speed up by only paying attention to delta data.
 */
fun getDecodedObjectFromTPS(of: Object): TransmittablePropertySequence{
    val klass = of.`class`
    val relevantICP = importantClassProtocols[klass] ?: throw Exception("No ICP is defined for $klass")
    val d = relevantICP.importantPropertyRetriever
    return d?.invoke(Retriever(of), of) ?: throw Exception("Coffee code lol I can't even think why this would happen")
}
