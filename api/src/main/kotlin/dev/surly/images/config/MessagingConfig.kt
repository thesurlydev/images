package dev.surly.images.config

import dev.surly.images.messaging.listener.LogConnectionEventListener
import dev.surly.images.messaging.listener.LogErrorEventListener
import io.nats.client.ConnectionListener
import io.nats.client.Nats
import io.nats.client.Options
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean

@ConfigurationProperties(prefix = "messaging")
data class MessagingConfig(val serverUrl: String, val topic: String) {

    @Bean
    fun options(): Options = io.nats.client.Options.Builder()
        .server(serverUrl)
        .errorListener(LogErrorEventListener())
        .connectionListener(LogConnectionEventListener())
        .build()

    @Bean(destroyMethod = "close")
    fun connection(options: Options): io.nats.client.Connection {
        return Nats.connect(options)
    }

}