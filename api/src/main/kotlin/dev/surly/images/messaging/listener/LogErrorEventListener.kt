package dev.surly.images.messaging.listener

import io.nats.client.Connection
import io.nats.client.ErrorListener

class LogErrorEventListener : ErrorListener {
    override fun exceptionOccurred(conn: Connection?, exp: Exception?) {
        println("Error: ${exp?.message}")
    }
}