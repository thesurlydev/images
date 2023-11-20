package dev.surly.images.model

import dev.surly.images.util.hashPassword

data class RegistrationRequest(val username: String, val password: String, val email: String)

fun RegistrationRequest.toUser(): User = User(
    username = this.username,
    passwordHash = this.password.hashPassword(),
    email = this.email,
)
