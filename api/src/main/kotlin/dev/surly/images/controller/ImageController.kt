package dev.surly.images.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.surly.images.config.ImageConfig
import dev.surly.images.config.MessagingConfig
import dev.surly.images.messaging.Publisher
import dev.surly.images.model.*
import dev.surly.images.service.AuditLogService
import dev.surly.images.service.ImageService
import dev.surly.images.service.StorageService
import dev.surly.images.util.FilePartExtensions.isValidMimeType
import dev.surly.images.util.FilePartExtensions.toByteArray
import dev.surly.images.util.FilePartExtensions.toImage
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.util.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    val messagingConfig: MessagingConfig,
    val imageService: ImageService,
    val imagesConfig: ImageConfig,
    val storageService: StorageService,
    val auditLogService: AuditLogService,
    val publisher: Publisher,
    val jackson: ObjectMapper
) {
    companion object {
        private val log = LoggerFactory.getLogger(ImageController::class.java)
    }

    @GetMapping("/{id}/download")
    suspend fun downloadImage(
        exchange: ServerWebExchange,
        @PathVariable id: UUID
    ): ResponseEntity<Any> {
        val userId = exchange.getAttribute<UUID>("userId")!!
        when (val image = imageService.findByUserIdAndId(userId, id)) {
            null -> {
                val msg = "Image not found: $id"
                auditLogService.logError(userId, "download", msg)
                val notFoundBody = mapOf(
                    "status" to "Not found",
                    "message" to msg
                )
                return ResponseEntity.status(404).body(notFoundBody)
            }

            else -> {
                val bytes = storageService.loadImage(image.path)
                val headers = HttpHeaders()
                headers.add("Content-Type", image.type)
                headers.add("Content-Disposition", "attachment; filename=${image.path}")
                auditLogService.logSuccess(userId, "download", "Downloaded image: $id")
                return ResponseEntity.ok().headers(headers).body(bytes)
            }
        }
    }

    @PostMapping("/upload")
    suspend fun uploadImage(
        exchange: ServerWebExchange,
        @RequestPart("file") filePart: FilePart
    ): ResponseEntity<Any> {
        val userId = exchange.getAttribute<UUID>("userId")!!
        // verify that the file is an image
        val mimeTypeValidationResult = filePart.isValidMimeType(imagesConfig.allowedMimeTypes)
        val detectedMimeType = mimeTypeValidationResult.mimeType
        if (!mimeTypeValidationResult.isValid) {
            val msg = "File is not an accepted type: $detectedMimeType"
            auditLogService.logError(userId, "upload", msg)
            return ResponseEntity.badRequest().body(mapOf("status" to "Bad request", "message" to msg))
        }

        // save the file to the storage service
        val bytes = filePart.toByteArray()
        val extension = detectedMimeType?.split("/")?.get(1)
        val saveResult = extension?.let { storageService.saveImage(bytes, it) }
        log.info("Saved image to '${storageService.getType()}' storage: $saveResult")

        // save the image to the database
        val originalImageName = filePart.filename()
        val image = detectedMimeType?.let { type ->
            saveResult?.let { sr ->
                filePart.toImage(
                    userId,
                    sr.location,
                    sr.sizeInBytes,
                    type,
                    sr.dimensions.first,
                    sr.dimensions.second,
                    null,
                    originalImageName
                )
            }
        }
        val savedImage = image?.let { imageService.saveImage(it) }
        log.info("Saved image to db: $savedImage")

        // publish an event to the message broker to perform the image processing
        val req = savedImage?.id?.let {
            ImageTransformRequest(
                it, listOf(
                    ScaleImageTransform(1280, 720),
                    RotateImageTransform(180.0)
                )
            )
        }
        val reqBytes = jackson.writeValueAsBytes(req)
        publisher.publish(messagingConfig.transformSubject, reqBytes)

        auditLogService.logSuccess(userId!!, "upload", "Uploaded image: ${savedImage?.id}")

        return ResponseEntity.ok(savedImage)
    }

    @GetMapping("/accepted-mime-types")
    suspend fun getAcceptedMimeTypes(): ResponseEntity<AcceptedMimeTypes> {
        val allowedMimeTypes = imagesConfig.allowedMimeTypes
        val acceptedTypes = AcceptedMimeTypes(allowedMimeTypes)
        return ResponseEntity.ok(acceptedTypes)
    }

    @GetMapping
    suspend fun getImages(exchange: ServerWebExchange): ResponseEntity<Flow<Image>> {
        val userId = exchange.getAttribute<UUID>("userId")!!
        val images = imageService.findByUserId(userId)
        return ResponseEntity.ok(images)
    }

    @GetMapping("/{id}")
    suspend fun getImageById(
        exchange: ServerWebExchange,
        @PathVariable id: UUID
    ): ResponseEntity<Image> {
        val userId = exchange.getAttribute<UUID>("userId")!!
        val image = imageService.findByUserIdAndId(userId, id)
        return if (image != null) ResponseEntity.ok(image)
        else ResponseEntity.notFound().build()
    }
}