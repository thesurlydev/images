package dev.surly.images.service

import dev.surly.images.config.StorageConfig
import dev.surly.images.model.SaveImageResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO
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
        val file = targetFilePath.toFile()

        // save the file to the target directory
        file.writeBytes(bytes)

        // now get the file size
        val fileSize = file.length()

        // get the dimensions
        val dimensions = withContext(Dispatchers.IO) {
            val bufImage = ImageIO.read(file)
            bufImage.let { image ->
                Pair(image.width, image.height)
            }
        }

        return SaveImageResult(
            location = uniqueFileName,
            sizeInBytes = fileSize,
            dimensions = dimensions
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