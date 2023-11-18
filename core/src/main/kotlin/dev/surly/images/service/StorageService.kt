package dev.surly.images.service

interface StorageService {
    suspend fun getType(): String
    suspend fun saveImage(bytes: ByteArray, mimeType: String): String
    suspend fun loadImage(location: String): ByteArray
}