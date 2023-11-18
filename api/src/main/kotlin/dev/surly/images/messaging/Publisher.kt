package dev.surly.images.messaging

interface Publisher {
    fun publish(subject: String, message: ByteArray)
}