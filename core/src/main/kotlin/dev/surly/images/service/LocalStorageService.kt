package dev.surly.images.service

import dev.surly.images.config.StorageConfig
import dev.surly.images.model.SaveImageResult
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories

@Service
@Profile("storage-local")
class LocalStorageService(val storageConfig: StorageConfig) : StorageService {

    override suspend fun getType(): String = "local"

    override suspend fun saveImage(bytes: ByteArray, extension: String): SaveImageResult {

        // sanity check to make sure the target directory is created
        val dir = Path.of(storageConfig.path)
        dir.createDirectories()

        // create a unique file name to avoid collisions
        val uniqueFileName = UUID.randomUUID().toString() + "." + extension
        val targetFilePath = dir.resolve(uniqueFileName)

        // save the file to the target directory
        targetFilePath.toFile().writeBytes(bytes)

        // now get the file size
        val fileSize = targetFilePath.toFile().length()

        return SaveImageResult(
            location = uniqueFileName,
            sizeInBytes = fileSize
        )
    }

    override suspend fun loadImage(location: String): ByteArray {
        Path.of(storageConfig.path).resolve(location).toFile().let { file ->
            file.inputStream().use { inputStream ->
                return inputStream.readAllBytes()
            }
        }
    }
}