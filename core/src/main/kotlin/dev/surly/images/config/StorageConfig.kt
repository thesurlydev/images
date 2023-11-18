package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "storage")
data class StorageConfig(
    val path: String
)