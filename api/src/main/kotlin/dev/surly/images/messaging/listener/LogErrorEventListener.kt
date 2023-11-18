package dev.surly.images.messaging.listener

import io.nats.client.Connection
import io.nats.client.ErrorListener
import org.slf4j.LoggerFactory

class LogErrorEventListener : ErrorListener {

    private val log = LoggerFactory.getLogger(LogErrorEventListener::class.java)
    override fun exceptionOccurred(conn: Connection?, exp: Exception?) {
        log.error("NATS: ${exp?.message}", exp)
    }
}