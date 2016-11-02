package com.gmail.caelum119.balloon.server


import com.gmail.caelum119.balloon.server.network_events.WorldChunkRequest
import com.gmail.caelum119.balloon.world.object_sync.ObjectSyncService
import com.gmail.caelum119.utils.event.NetworkEvent
import com.gmail.caelum119.utils.network.server.TCPServer
import java.util.*

/**
 * First created 9/27/2016 in BalloonEngine
 */
class BalloonServer(val serverSettings: ServerSettings) {
    val world = ServerEngineInstance()
    val server = TCPServer(4030)
    val connectedClients = ArrayList<ConnectedBalloonClient>()

    init {
        ObjectSyncService.server = true //Limit the effect of status updates from the client on ObjectSyncService
        server.start()
        server.addConnectionEstablishedListener {
            ObjectSyncService.recipients.add(it.connection)
            connectedClients.add(ConnectedBalloonClient(it.connection))

            it.connection.addListener(this, this)
        }
    }

    @NetworkEvent fun serveWorldChunkRequest(worldChunkRequest: WorldChunkRequest) {

    }
}