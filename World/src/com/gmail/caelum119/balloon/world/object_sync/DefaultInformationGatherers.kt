package com.gmail.caelum119.balloon.world.object_sync

import com.bulletphysics.collision.dispatch.CollisionObject
import java.util.*
import kotlin.reflect.KClass

/**
 * First created 10/9/2016 in BalloonEngine
 */
class ExampleTransmittableClass() {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    init {
        defineSyncMethod<ExampleTransmittableClass, TransmittablePropertySequence> { saidObject ->

            addProperties(ExampleTransmittableClass::x, ExampleTransmittableClass::y)
            addProperty(ExampleTransmittableClass::x)
            addProperty(ExampleTransmittableClass::y)
            addProperty(ExampleTransmittableClass::z)

            importantPropertyRetriever = {
                getTPSFromProperties(saidObject)
            }

            instanceBuilder {
                val exampleTransmittableClass = ExampleTransmittableClass()
                reflectValuesOnto(exampleTransmittableClass)
                return@instanceBuilder exampleTransmittableClass
            }
        }
    }
}

object DefaultInformationGatherers {
    val informationGathererMap = HashMap<KClass<*>, (Any) -> TransmittablePropertySequence>()

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

    inline fun <reified T> addInformationGatherer(noinline transmittablePropertySequence: (T) -> TransmittablePropertySequence): Unit {
        informationGathererMap.put(T::class, transmittablePropertySequence as (Any) -> TransmittablePropertySequence)
    }
}