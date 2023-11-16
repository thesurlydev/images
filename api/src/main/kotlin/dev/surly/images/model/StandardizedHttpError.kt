package dev.surly.images.model

import java.time.OffsetDateTime
import java.util.UUID

data class StandardizedHttpError(val message: String,
                                 val id: UUID = UUID.randomUUID(),
                                 val timestamp: OffsetDateTime = OffsetDateTime.ofInstant(java.time.Instant.now(), java.time.ZoneOffset.UTC))
