package com.gmail.caelum119.balloon.world.object_sync

import com.bulletphysics.collision.dispatch.CollisionObject
import java.util.*
import kotlin.reflect.KClass

/**
 * First created 10/9/2016 in BalloonEngine
 */
class ExampleTransmittableClass(){
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    init {
        defineSyncableRuleset<ExampleTransmittableClass, test> { saidObject ->

            addProperty(ExampleTransmittableClass::x)
            addProperty(ExampleTransmittableClass::y)
            addProperty(ExampleTransmittableClass::z)

            setRetriever {
                it.x
            }

            builder {
                val exampleTransmittableClass = ExampleTransmittableClass()
                reflectValuesOnto(exampleTransmittableClass)
                return@builder exampleTransmittableClass
            }
        }
    }
}

object InformationGatherers {
    val informationGathererMap = HashMap<KClass<*>, (Any) -> TransmittablePropertySequence<*>>()
    init {

        /**
         * Primitives
         */

        /**
         * Physics
         */
        addInformationGatherer<CollisionObject> {
            getTransmittablePropertySequence<CollisionObject>(
                    it.friction,
                    it.hitFraction,
                    it.collisionShape
            )
        }
    }

    inline fun <reified T> addInformationGatherer(noinline transmittablePropertySequence: (T) -> TransmittablePropertySequence<T>): Unit {
        informationGathererMap.put(T::class, transmittablePropertySequence as (Any) -> TransmittablePropertySequence<T>)
    }
}

data class test(var d: Int)
class test2() : TransferableObjectImpl() {
    var test3: Int by networkSyncable({ 4 }, this)

    init {
        test3++
    }
}

fun main(args: Array<String>) {
    test2()
}