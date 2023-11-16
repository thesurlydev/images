package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("users")
data class User(
    @Id val id: UUID,
    @Column("username") val username: String,
    @Column("email") val email: String,
)