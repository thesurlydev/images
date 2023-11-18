package dev.surly.images.service

import dev.surly.images.model.SaveImageResult

interface StorageService {

    suspend fun getType(): String
    suspend fun saveImage(bytes: ByteArray, extension: String): SaveImageResult
    suspend fun loadImage(location: String): ByteArray
}