package com.gmail.caelum119.utils.network

import com.gmail.caelum119.utils.event.NetworkEvent
import com.gmail.caelum119.utils.network.tags.MessageTag
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

/**

 */
fun main(args: Array<String>) {
    TCPClient("127.0.0.1", 30001).start()
}

open class TCPClient(address: String, port: Int) : Thread() {
    val connectionToServer: ConnectionToServer

    init {
        connectionToServer = ConnectionToServer(TCPConnection(Socket(address, port)))
        connectionToServer.connection.addListener(this, this)
    }

    override fun start() {
        connectionToServer.start()
    }

    fun listenToServer() {

        while (true) {
            val inFromUser = BufferedReader(InputStreamReader(System.`in`))
            val sentence = inFromUser.readLine()

            println("Out to server: " + sentence)
            connectionToServer.connection.sendPacketImmediately(*MessageTag(sentence).getData())
        }
    }

    @NetworkEvent
    fun onMessageReceive(messageTag: MessageTag) {
        println("Client received " + messageTag.message)
    }
}
