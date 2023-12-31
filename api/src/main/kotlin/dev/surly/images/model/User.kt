package dev.surly.images.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("users")
data class User(
    @Id val id: UUID? = null,
    @Column("username") val username: String,
    @Column("password_hash") val passwordHash: String,
    @Column("email") val email: String,
)

fun User.toWhoAmIResponse(): WhoAmIResponse =
    WhoAmIResponse(this.id!!, this.username, this.email)