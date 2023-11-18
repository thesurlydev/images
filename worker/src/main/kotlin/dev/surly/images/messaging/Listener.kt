package dev.surly.images.messaging

interface Listener {
    fun handleMessage(data: ByteArray)
}