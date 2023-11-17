package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.UUID

@ConfigurationProperties(prefix = "auth")
data class AuthConfig(val testUserId: UUID)
