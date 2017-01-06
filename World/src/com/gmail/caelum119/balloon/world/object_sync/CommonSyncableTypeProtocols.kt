package com.gmail.caelum119.balloon.world.object_sync

/**
 * First created 11/6/2016 in BalloonEngine
 */
object CommonSyncableTypeProtocols {
    init {
        defineSyncMethod<List<*>, TransmittablePropertySequence> { saidInstance ->
            instanceBuilder {
                saidInstance
            }

            importantPropertyRetriever {
                return@importantPropertyRetriever getTPSFromProperties(saidInstance)
            }
        }
    }
}