package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "messaging")
data class MessagingConfig(val topic: String) {
}