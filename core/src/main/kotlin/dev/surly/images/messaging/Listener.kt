package dev.surly.images.messaging

interface Listener {
    suspend fun handleMessage(data: ByteArray)
}