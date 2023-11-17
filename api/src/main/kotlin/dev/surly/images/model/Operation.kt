package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("operations")
data class Operation(
    @Id val id: UUID,
    @Column("name") val name: String,
    @Column("description") val description: String,
)
