package dev.surly.images.service

import dev.surly.images.model.SaveImageResult
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("storage-s3")
class S3StorageService : StorageService {

    override suspend fun getType(): String = "s3"
    override suspend fun saveImage(bytes: ByteArray, extension: String): SaveImageResult {
        TODO("Not yet implemented")
    }

    override suspend fun loadImage(location: String): ByteArray {
        TODO("Not yet implemented")
    }
}