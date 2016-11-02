package com.gmail.caelum119.utils.event

/**

 */
@Retention(AnnotationRetention.RUNTIME)
annotation class NetworkEvent(
        /**
         * The amount of times the event can be received before cancelling itself.
         * *Note: `ignoreCancelled` makes no difference to this.*
         */
        val limit: Int = -1,
        /**
         * Whether or not to ignore a event if it's cancelled, true will not receive cancelled events and false will receive all events.
         * *Note: if a event reaches it's `limit`, this value makes no difference.*
         */
        val ignoreCancelled: Boolean = true)
