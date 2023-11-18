package dev.surly.images.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.surly.images.config.AuthConfig
import dev.surly.images.config.ImageConfig
import dev.surly.images.config.MessagingConfig
import dev.surly.images.messaging.Publisher
import dev.surly.images.model.AcceptedMimeTypes
import dev.surly.images.model.Image
import dev.surly.images.model.ImageTransform
import dev.surly.images.model.ImageTransformRequest
import dev.surly.images.service.ImageService
import dev.surly.images.service.StorageService
import dev.surly.images.util.FilePartExtensions.isValidMimeType
import dev.surly.images.util.FilePartExtensions.toByteArray
import dev.surly.images.util.FilePartExtensions.toImage
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    val authConfig: AuthConfig,
    val messagingConfig: MessagingConfig,
    val imageService: ImageService,
    val imagesConfig: ImageConfig,
    val storageService: StorageService,
    val publisher: Publisher,
    val jackson: ObjectMapper
) {
    private val log = LoggerFactory.getLogger(ImageController::class.java)


    @PostMapping("/upload")
    suspend fun uploadImage(@RequestPart("file") filePart: FilePart): ResponseEntity<Image> {

        // verify that the file is an image
        val mimeTypeValidationResult = filePart.isValidMimeType(imagesConfig.allowedMimeTypes)
        val detectedMimeType = mimeTypeValidationResult.mimeType
        if (!mimeTypeValidationResult.isValid) {
            return ResponseEntity.badRequest().build()
        }
        log.info("File is an accepted type: $detectedMimeType")

        // TODO prevent very large files from being uploaded

        // save the file to the storage service
        val bytes = filePart.toByteArray()
        val savedFileName = storageService.saveImage(bytes, detectedMimeType!!)
        log.info("Saved image location: $savedFileName")

        // FIXME capture actual user id after authentication is implemented
        val userId = authConfig.testUserId

        // save the image to the database
        val fileSize = bytes.size.toLong()
        val image = filePart.toImage(userId, savedFileName, fileSize, detectedMimeType)
        val savedImage = imageService.saveImage(image)
        log.info("Saved image to db: $savedImage")

        // publish an event to the message broker to perform the image processing
        val req = ImageTransformRequest(
            UUID.randomUUID(), listOf(
                ImageTransform("resize", hashMapOf("width" to 1280, "height" to 720)),
                ImageTransform("rotate", hashMapOf("degrees" to 180.0))
            )
        )
        val reqBytes = jackson.writeValueAsBytes(req)
        publisher.publish(messagingConfig.transformSubject, reqBytes)

        return ResponseEntity.ok(savedImage)
    }

    @GetMapping("/accepted")
    suspend fun getAcceptedImageTypes(): ResponseEntity<AcceptedMimeTypes> {
        val allowedMimeTypes = imagesConfig.allowedMimeTypes
        val acceptedTypes = AcceptedMimeTypes(allowedMimeTypes)
        return ResponseEntity.ok(acceptedTypes)
    }

    @GetMapping
    suspend fun getImages(): ResponseEntity<Flow<Image>> {
        // FIXME capture actual user id after authentication is implemented
        val userId = authConfig.testUserId
        val images = imageService.findByUser(userId)
        return ResponseEntity.ok(images)
    }

    @GetMapping("/{id}")
    suspend fun getImageById(@PathVariable id: UUID): ResponseEntity<Image> {
        val image = imageService.findById(id)
        return if (image != null) ResponseEntity.ok(image)
        else ResponseEntity.notFound().build()
    }
}