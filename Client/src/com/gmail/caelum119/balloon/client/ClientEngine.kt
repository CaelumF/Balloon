package com.gmail.caelum119.balloon.client

import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.object_sync.TransmittablePropertySequence
import com.gmail.caelum119.balloon.world.object_sync.defineSyncMethod

/**
 * First created 10/13/2016 in BalloonEngine
 */
class ClientEngine : EngineInstance(){

    init {
        defineSyncMethod<EngineInstance, TransmittablePropertySequence> { saidInstance ->
            addProperties(ClientEngine::allSyncCategories)
            importantPropertyRetriever {
                return@importantPropertyRetriever getTPSFromProperties(it)
            }

            instanceBuilder { instanceData ->
                val instance = ClientEngine()
                reflectValuesOnto(instance)
                return@instanceBuilder instance
            }
        }
    }

    fun connect(){

    }
}