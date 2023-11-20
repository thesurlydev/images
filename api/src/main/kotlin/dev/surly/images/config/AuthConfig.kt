package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth")
data class AuthConfig(
    val secretKey: String,
    val logRequests: Boolean,
    val enabled: Boolean,
    val excludedPaths: Set<String>
)
