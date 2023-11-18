package dev.surly.images.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table("images")
data class Image(
    @Id var id: UUID? = null,
    @Column("user_id") @JsonProperty("user_id") val userId: UUID,
    val path: String,
    val status: String = "processing",
    val type: String,
    @Column("file_size") @JsonProperty("file_size") val fileSizeBytes: Long,
    @Column("create_timestamp") @JsonProperty("created_at") var createdAt: OffsetDateTime? = null
) {
}