package com.gmail.caelum119.balloon.server


import com.gmail.caelum119.balloon.server.network_events.WorldChunkRequest
import com.gmail.caelum119.balloon.world.object_sync.ObjectSyncService
import com.gmail.caelum119.balloon.world.object_sync.tags.WorldSyncTag
import com.gmail.caelum119.utils.event.NetworkEvent
import com.gmail.caelum119.utils.network.server.TCPServer
import java.util.*

/**
 * First created 9/27/2016 in BalloonEngine
 */
class BalloonServer(val serverSettings: ServerSettings) {
    val world = ServerEngine()
    val server = TCPServer(4030)
    val connectedClients = ArrayList<ConnectedBalloonClient>()

    init {
        ObjectSyncService.server = true //Limit the effect of status updates from the client on ObjectSyncService
        server.start()

        server.addConnectionEstablishedListener { newClient ->
            ObjectSyncService.recipients.add(newClient.connection)
            connectedClients.add(ConnectedBalloonClient(newClient.connection))
            newClient.connection.addListener(this, this)

            //Send entire current world.
            //TODO: Filter out information irrelevant to that specific client(extremely distant objects, etc.)
            newClient.connection.sendTag(WorldSyncTag(world.allSyncCategories))
        }
    }

    @NetworkEvent fun serveWorldChunkRequest(worldChunkRequest: WorldChunkRequest) {

    }
}