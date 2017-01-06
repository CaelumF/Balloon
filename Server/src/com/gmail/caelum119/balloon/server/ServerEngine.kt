package com.gmail.caelum119.balloon.server

import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.object_sync.TransmittablePropertySequence
import com.gmail.caelum119.balloon.world.object_sync.defineSyncMethod

/**
 * First created 10/13/2016 in BalloonEngine
 */
class ServerEngine : EngineInstance() {

    init {
        defineSyncMethod<EngineInstance, TransmittablePropertySequence> { saidInstance ->
            addProperties(ServerEngine::allSyncCategories)
            importantPropertyRetriever {
                return@importantPropertyRetriever getTPSFromProperties(it)
            }

            instanceBuilder { instanceData ->
                val instance = ServerEngine()
                reflectValuesOnto(instance)
                return@instanceBuilder instance
            }
        }
    }

//    override fun getData(): {
//
//    }
}