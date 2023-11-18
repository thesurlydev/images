package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table("image_operations")
data class ImageOperation(
    @Id val id: UUID,
    @Column("image_id") val imageId: UUID,
    @Column("operation_id") val operationId: UUID,
    @Column("parameters") val parameters: String,
    @Column("path") val path: String,
    @Column("status") val status: String = "processing",
    @Column("create_timestamp") val created: OffsetDateTime? = null) {
}