package com.gmail.caelum119.utils.network.server

import com.gmail.caelum119.utils.network.ConnectionToClient
import com.gmail.caelum119.utils.network.TCPConnection
import java.net.ServerSocket
import java.net.Socket
import java.util.*

//TODO: Event register for network tags
/**

 */
class TCPServer(val hostPort: Int) : Thread() {
  private var connections = ArrayList<ConnectionToClient>()
  private val onConnectionEstablished = ArrayList<(ConnectionToClient) -> Unit>()

  override fun run() {
    super.run()
    val welcomeSocket: ServerSocket = ServerSocket(hostPort)
    while (true) {
      val specificSocket: Socket = welcomeSocket.accept()
      //Wait until a packet is actually received.
      val clientConnection = ConnectionToClient(TCPConnection(specificSocket))
      Thread(clientConnection.connection).start()
      clientConnection.connection.addListener(this, this)

      for(listener in onConnectionEstablished){
        listener.invoke(clientConnection)
      }
    }
  }

  public fun addConnectionEstablishedListener(listener: (ConnectionToClient) -> Unit){
    onConnectionEstablished.add(listener)
  }
}
