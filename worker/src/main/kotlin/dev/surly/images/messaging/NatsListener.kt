package dev.surly.images.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.surly.images.config.MessagingConfig
import dev.surly.images.model.ImageTransformRequest
import dev.surly.images.service.ImageTransformService
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
class NatsListener(
    val messagingConfig: MessagingConfig,
    val jackson: ObjectMapper,
    val imageTransformService: ImageTransformService
) : Listener {

    companion object {
        private val log = LoggerFactory.getLogger(NatsListener::class.java)
    }

    private lateinit var connection: Connection
    private lateinit var subscription: io.nats.client.Subscription

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun init() {
        connection = Nats.connect(messagingConfig.options())
        subscription = connection.subscribe(messagingConfig.transformSubject)

        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val message = subscription.nextMessage(Duration.ofSeconds(1)) ?: continue
                handleMessage(message.data)
            }
        }
    }

    override suspend fun handleMessage(data: ByteArray) {
        log.info("NATS: received message on '${messagingConfig.transformSubject}' subject: ${String(data)}")
        convertByteArrayToImageTransformRequest(data).let { request ->
            imageTransformService.transform(request)
        }
    }

    private fun convertByteArrayToImageTransformRequest(data: ByteArray): ImageTransformRequest {
        val mapper = jackson.registerKotlinModule()
        return mapper.readValue<ImageTransformRequest>(data)
    }
}