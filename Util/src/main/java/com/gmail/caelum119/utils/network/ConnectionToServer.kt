package com.gmail.caelum119.utils.network


import sun.security.rsa.RSAPublicKeyImpl

/**
 * @param socket The port to open for communication with the server.
 *
 * With TCP, the connection is established by the protocol.
 */
//TODO: Maybe separate the utility and event system from the actual connection code to avoid duplicated code
class ConnectionToServer(val connection: Connection) {

    init {
        connection.sendPacketImmediately(*(connection.keys.public as RSAPublicKeyImpl).encoded)
        connection.theirPublicKey = RSAPublicKeyImpl(connection.waitForPacket())
    }

    /**
     * Makes the thread start listening.
     *
     * TODO: Perhaps optimise ConnectionToClient's start() and match here.
     */
    fun start(): Thread {
        val thread = Thread(connection)
        thread.start()
        return thread
    }
}