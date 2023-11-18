package dev.surly.images.service

import dev.surly.images.model.ImageTransformRequest
import dev.surly.images.process.ImageProcessor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ImageTransformService(val imageProcessor: ImageProcessor) {

    companion object {
        private val log = LoggerFactory.getLogger(ImageTransformService::class.java)
    }

    suspend fun transform(req: ImageTransformRequest) {
        log.info("TRFM: received transform request: $req")

        // TODO load image from database

        // TODO load image from storage

        // TODO transform image

        // TODO save transformed image to storage

        // TODO create batch record in db

        // TODO create operation records in db

        // TODO save transform image to db

    }
}