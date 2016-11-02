package com.gmail.caelum119.balloon.world.engine

import com.gmail.caelum119.balloon.world.scenegraph.Chunk
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * First created 3/27/2016 in Engine
 */
abstract class EngineInstance {
    val chunks = ArrayList<Chunk>()

    init {
        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay({
            chunks.forEach(Chunk::tick)
        }, 16.toLong(), 16.toLong(), TimeUnit.MILLISECONDS)
    }

    fun getWholeTransferable(): {

    }
}

fun ArrayList<Chunk>.getAll(){

}