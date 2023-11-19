package dev.surly.images.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime
import java.util.*

@Table("audit_logs")
data class AuditLog(
    @Id var id: UUID? = null,
    @Column("user_id") @JsonProperty("user_id") val userId: UUID,
    val operation: String,
    val status: String,
    val message: String,
    @Column("create_timestamp") @JsonProperty("created_at") var create_timestamp: OffsetDateTime? = null
)
