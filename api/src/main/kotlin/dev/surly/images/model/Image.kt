package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("images")
class Image(@Id val id: UUID,
            @Column("user_id") val userId: UUID,
            @Column("path") val path: String,) {
}