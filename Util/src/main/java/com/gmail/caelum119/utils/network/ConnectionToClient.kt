package com.gmail.caelum119.utils.network

import sun.security.rsa.RSAPublicKeyImpl

/**
 * The type os this connection is determined by whether it is constructed with a TCPSocket or DatagramSocket.
 *@param UDPSocket The socket to listen from, should be publicised as this is a server and Clients must know it.
 */
class ConnectionToClient(val connection: Connection) {

    init {
        //Receive the Client's public key and send ours)
        connection.theirPublicKey = connection.getPublicKey(connection.waitForPacket())
        connection.sendPacketImmediately(*(connection.keys.public as RSAPublicKeyImpl).encoded)
    }

    /**
     * Starts the connection, so that it listens to events.
     * TODO: Perhaps optimise so that each connection shares a single thread, might increase performance on servers with several connections and prevent connection-thread spamming.
     */
    fun start(): Thread {
        val thread = Thread(connection)
        thread.start()
        return thread
    }
}