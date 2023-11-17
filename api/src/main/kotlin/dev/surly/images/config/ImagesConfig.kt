package dev.surly.images.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path

@ConfigurationProperties(prefix = "images")
data class ImagesConfig(val storagePath: Path, val allowedTypes: List<String>)