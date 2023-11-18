package dev.surly.images.messaging

import io.nats.client.Connection
import org.springframework.stereotype.Service

@Service
class NatsPublisher(val conn: Connection) : Publisher {

    override fun publish(topic: String, message: ByteArray) {
        conn.publish(topic, message)
    }
}