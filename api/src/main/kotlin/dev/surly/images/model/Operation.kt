package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("operations")
data class Operation(
    @Id val id: UUID,
    val name: String,
    val description: String,
)
