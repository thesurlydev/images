package dev.surly.images.config

import dev.surly.images.messaging.listener.LogConnectionEventListener
import dev.surly.images.messaging.listener.LogErrorEventListener
import io.nats.client.Options
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "messaging")
data class MessagingConfig(
    val serverUrl: String,
    val transformSubject: String
) {

    fun options(): Options = Options.Builder()
        .server(serverUrl)
        .errorListener(LogErrorEventListener())
        .connectionListener(LogConnectionEventListener())
        .build()
}