package com.gmail.caelum119.balloon.server


import com.gmail.caelum119.balloon.world.object_sync.ObjectSyncService
import com.gmail.caelum119.balloon.world.object_sync.tags.ClientIdentifyAs
import com.gmail.caelum119.utils.event.NetworkEvent
import com.gmail.caelum119.utils.network.server.TCPServer
import java.util.*

/**
 * First created 9/27/2016 in BalloonEngine
 */
class BalloonServer(val serverSettings: ServerSettings) {
    val world = ServerEngine()
    val server = TCPServer(45839)
    val connectedClients = ArrayList<ConnectedBalloonClient>()

    init {
        ObjectSyncService.server = true //Limit the effect of status updates from the client on ObjectSyncService
        server.start()

        server.addConnectionEstablishedListener { newClient ->
            ObjectSyncService.recipients.add(newClient.connection)
            connectedClients.add(ConnectedBalloonClient(newClient.connection))
            newClient.connection.addListener(this, this)
        }
    }

    @NetworkEvent fun identifyClient(identifyingClient: ClientIdentifyAs){
        identifyingClient
    }
}

fun main(args: Array<String>) {
    BalloonServer(ServerSettings())
}