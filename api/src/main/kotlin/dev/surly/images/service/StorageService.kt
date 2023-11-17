package dev.surly.images.service

interface StorageService {
    suspend fun getType(): String
    suspend fun saveImage(bytes: ByteArray, mimeSubtype: String): String
    suspend fun loadImage(location: String): ByteArray
}