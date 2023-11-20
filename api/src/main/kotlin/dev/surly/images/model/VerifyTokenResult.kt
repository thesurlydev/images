package dev.surly.images.model

import java.util.UUID

data class VerifyTokenResult(val isValid: Boolean, val userId: UUID? = null)
