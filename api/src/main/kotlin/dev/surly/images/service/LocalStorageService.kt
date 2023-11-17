package dev.surly.images.service

import dev.surly.images.config.StorageConfig
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories

@Service
@Profile("storage-local")
class LocalStorageService(val storageConfig: StorageConfig) : StorageService {

    private val log = LoggerFactory.getLogger(LocalStorageService::class.java)
    override suspend fun getType(): String = "local"

    override suspend fun saveImage(bytes: ByteArray, mimeType: String): String {

        // sanity check to make sure the target directory is created
        val dir = Path.of(storageConfig.path)
        dir.createDirectories()

        // create a unique file name to avoid collisions
        val extension = mimeType.substringAfter("/")
        val uniqueFileName = UUID.randomUUID().toString() + "." + extension
        val targetFilePath = dir.resolve(uniqueFileName)

        // save the file to the target directory
        log.info("Saving file to: $targetFilePath")
        targetFilePath.toFile().writeBytes(bytes)

        return targetFilePath.absolutePathString()
    }

    override suspend fun loadImage(location: String): ByteArray {
        File(location).inputStream().use { inputStream ->
            return inputStream.readAllBytes()
        }
    }
}