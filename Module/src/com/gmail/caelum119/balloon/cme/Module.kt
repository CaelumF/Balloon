package com.gmail.caelum119.balloon.cme

import com.gmail.caelum119.utils.event.EventCollection
import com.gmail.caelum119.utils.event.EventType
import com.gmail.caelum119.utils.event.ListenerInterface

/**
 * First created 2/5/2017 in BalloonEngine
 */
open class Module {
    private val eventCollection = EventCollection()
    val events = ListenerInterface(eventCollection)

    fun engineStart(){

    }

    fun clEngineStart(){

    }

    fun svEngineStart(){

    }

    open class ModulesEvents(): EventType(){

    }
    //... possibly more entry point methods for modules
}