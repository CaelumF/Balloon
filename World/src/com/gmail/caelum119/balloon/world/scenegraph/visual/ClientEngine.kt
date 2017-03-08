package com.gmail.caelum119.balloon.world.scenegraph.visual

import com.gmail.caelum119.balloon.world.engine.EngineInstance
import com.gmail.caelum119.balloon.world.object_sync.tags.ClientIdentifyAs
import com.gmail.caelum119.utils.doLater
import com.gmail.caelum119.utils.network.ConnectionToServer
import java.util.concurrent.TimeUnit

/**
 * First created 10/13/2016 in BalloonEngine
 */
class ClientEngine(var connectionToServer: ConnectionToServer) : EngineInstance(){

    init {
        connectionToServer.start()
        doLater(1, TimeUnit.SECONDS, {
            connectionToServer.connection.sendTag(ClientIdentifyAs("test"))
        })
    }
}