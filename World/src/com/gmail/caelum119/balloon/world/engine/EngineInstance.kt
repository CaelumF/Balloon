package com.gmail.caelum119.balloon.world.engine

import com.gmail.caelum119.balloon.world.engine.components.Component
import com.gmail.caelum119.balloon.world.object_sync.SyncCategory
import com.gmail.caelum119.balloon.world.scenegraph.Chunk
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * First created 3/27/2016 in Engine
 */
abstract class EngineInstance {
    var chunks = ArrayList<Chunk>()
    /**
     *  All data that is relevant to the clients should be contained in here. Including data that is already stored
     *  elsewhere.
     */
    var allSyncCategories = SyncCategory<SyncCategory<*>>()
    var allComponents = HashMap<Class<*>, ArrayList<Component>>()

    init {

        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay({
            chunks.forEach(Chunk::tick)
        }, 16.toLong(), 16.toLong(), TimeUnit.MILLISECONDS)
    }
}