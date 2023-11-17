package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "images")
data class ImageConfig(
    val allowedMimeTypes: Set<String>
)