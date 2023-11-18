package dev.surly.images.messaging

interface Publisher {
    fun publish(topic: String, message: ByteArray)
}