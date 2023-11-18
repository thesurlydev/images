package dev.surly.images.messaging

import dev.surly.images.config.MessagingConfig
import io.nats.client.Connection
import io.nats.client.Nats
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class NatsPublisher(val messagingConfig: MessagingConfig) : Publisher {

    companion object {
        private val log = LoggerFactory.getLogger(NatsPublisher::class.java)
    }

    override fun publish(subject: String, message: ByteArray) {
        try {
            log.info("NATS: publishing message to '${subject}' subject")
            Nats.connect(messagingConfig.options()).use { nc: Connection ->
                nc.publish(subject, message)

                /**
                 * If called while the connection is disconnected due to network issues this method will wait for up
                 * to the timeout for a reconnect or close.
                 */
                nc.flush(Duration.ofSeconds(5))
            }
            log.info("NATS: published message to '${subject}' subject")
        } catch (e: Exception) {
            log.error("NATS: error publishing to '${subject}' subject", e)
        }
    }
}