package dev.surly.images.model

data class AuthenticatedUser(val user: User, val token: String)
