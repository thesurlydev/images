package dev.surly.images.model

import org.springframework.http.MediaType

data class MediaTypeValidationResult(val mediaType: String?, val isValid: Boolean)
