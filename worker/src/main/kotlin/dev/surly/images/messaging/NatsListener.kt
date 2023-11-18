package dev.surly.images.messaging

import dev.surly.images.config.MessagingConfig
import io.nats.client.Connection
import io.nats.client.Nats
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class NatsListener(val messagingConfig: MessagingConfig) : Listener {

    companion object {
        private val log = LoggerFactory.getLogger(NatsListener::class.java)
    }

    private lateinit var connection: Connection
    private lateinit var subscription: io.nats.client.Subscription

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun init(){
        connection = Nats.connect(messagingConfig.options())
        subscription = connection.subscribe(messagingConfig.transformSubject)

        GlobalScope.launch(Dispatchers.IO) {
            while(true) {
                val message = subscription.nextMessage(Duration.ofSeconds(1)) ?: continue
                handleMessage(message.data)
            }
        }
    }

    override fun handleMessage(data: ByteArray) {
        log.info("NATS: received message on '${messagingConfig.transformSubject}' subject")
        log.info("NATS: message: ${String(data)}")
    }
}