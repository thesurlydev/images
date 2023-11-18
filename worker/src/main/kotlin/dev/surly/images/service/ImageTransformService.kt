package dev.surly.images.service

import dev.surly.images.model.Image
import dev.surly.images.model.ImageTransformRequest
import dev.surly.images.process.ImageProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ImageTransformService(
    val imageProcessor: ImageProcessor,
    val imageService: ImageService,
    val storageService: StorageService
) {

    companion object {
        private val log = LoggerFactory.getLogger(ImageTransformService::class.java)
    }

    suspend fun transform(req: ImageTransformRequest) {
        log.info("TRFM: received transform request: $req")

        // load image from database
        val imageDb = imageService.findById(req.imageId)
        log.info("TRFM: db image: ${imageDb.toString()}")

        // load image from storage
        val imageBytes = imageDb?.let { storageService.loadImage(it.path) }
        log.info("TRFM: loaded image from ${storageService.getType()} storage")

        // apply transforms
        val formatName = imageDb?.type?.split("/")?.get(1)
        val transformedByteArray = formatName?.let { imageProcessor.transformImage(imageBytes!!, formatName, req) }
        log.info("TRFM: transformed image")

        // save transformed image to storage
        val saveResult = storageService.saveImage(transformedByteArray!!, formatName)
        log.info("TRFM: resized save result: $saveResult")

        // save transformed image to db
        val transformedImage = Image(
            id = null,
            userId = imageDb.userId,
            path = saveResult.location,
            "complete",
            type = imageDb.type,
            fileSizeBytes = saveResult.sizeInBytes,
            createdAt = null
        )
        val transformedImageDb = imageService.saveImage(transformedImage)
        log.info("TRFM: saved transformed image to db: $transformedImageDb")

        // TODO create batch record in db

        // TODO create operation records in db

        // TODO save transform image to db

    }
}