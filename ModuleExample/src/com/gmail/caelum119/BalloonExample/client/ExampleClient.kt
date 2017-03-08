package com.gmail.caelum119.BalloonExample.client

import com.gmail.caelum119.balloon.client.render.a3d.A3DVBOWorldRenderer
import com.gmail.caelum119.balloon.client.render.a3d.Ardor3DRendererBase
import com.gmail.caelum119.balloon.world.scenegraph.visual.ClientEngine
import com.gmail.caelum119.utils.network.ConnectionToServer
import com.gmail.caelum119.utils.network.TCPConnection
import java.net.Socket

/**
 * First created 2/18/2017 in BalloonEngine
 */
object ExampleClient{
    val clientEngine = ClientEngine(ConnectionToServer(TCPConnection(Socket("localhost", 45839))))
    val renderer = A3DVBOWorldRenderer(clientEngine)

    @JvmStatic
    fun main(args: Array<String>) {
        print("starting")
        Ardor3DRendererBase.start(renderer)
        clientEngine
    }
}
