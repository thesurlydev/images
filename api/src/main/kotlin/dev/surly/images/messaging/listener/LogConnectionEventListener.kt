package dev.surly.images.messaging.listener

import io.nats.client.Connection
import io.nats.client.ConnectionListener
import org.slf4j.LoggerFactory

class LogConnectionEventListener : ConnectionListener {

    private val log = LoggerFactory.getLogger(LogConnectionEventListener::class.java)
    override fun connectionEvent(conn: Connection?, event: ConnectionListener.Events?) {
        log.info("Connection event: ${event?.name}")
    }
}