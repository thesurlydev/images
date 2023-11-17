package dev.surly.images.service

import dev.surly.images.config.StorageConfig
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("storage-local")
class LocalStorageService(val storageConfig: StorageConfig) : StorageService {

    private val log = LoggerFactory.getLogger(LocalStorageService::class.java)
    override suspend fun getType(): String = "local"

    override suspend fun saveImage(bytes: ByteArray): String {

        // sanity check to make sure the target directory is created
        /*val dir = Path.of(storageConfig.path)
        dir.createDirectories()

        // create a unique file name
        val fileExtension = filePart.filename().substringAfterLast(".")
        val uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension
        val targetFilePath = dir.resolve(uniqueFileName)

        // save the file to the target directory

            log.info("Saving file to: $targetFilePath")

            filePart.transferTo(targetFilePath).awaitSingleOrNull()*/
        TODO("Not yet implemented")

    }

    override suspend fun loadImage(location: String): ByteArray {
        TODO("Not yet implemented")
    }

}