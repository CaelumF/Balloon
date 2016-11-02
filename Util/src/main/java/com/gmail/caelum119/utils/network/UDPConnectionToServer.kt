package com.gmail.caelum119.utils.network


import sun.security.rsa.RSAPublicKeyImpl
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * @param socket The port to open for communication with the server.
 *
 * With TCP, the connection is established by the protocol. TODO: Allow connections to be established via UDP?
 */
public class UDPConnectionToServer(socket: DatagramSocket,  theirAddress: InetAddress? = null, theirPort: Int? = null)
: UDPConnection(socket, theirAddress, theirPort), Runnable{

  init {
    sendPacketImmediately(*(keys.public as RSAPublicKeyImpl).encoded)
    theirPublicKey = RSAPublicKeyImpl(waitForPacket())
  }
}