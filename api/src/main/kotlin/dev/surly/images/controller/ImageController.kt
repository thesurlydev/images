package dev.surly.images.controller

import dev.surly.images.config.ImageConfig
import dev.surly.images.model.AcceptedImageTypes
import dev.surly.images.model.Image
import dev.surly.images.service.ImageService
import dev.surly.images.service.StorageService
import dev.surly.images.util.FilePartExtensions.isValidMimeType
import dev.surly.images.util.FilePartExtensions.toByteArray
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageService: ImageService,
    private val imagesConfig: ImageConfig,
    private val storageService: StorageService
) {
    private val log = LoggerFactory.getLogger(ImageController::class.java)

    @PostMapping("/upload")
    suspend fun uploadImage(@RequestPart("file") filePart: FilePart): ResponseEntity<String> {

        // verify that the file is an image
        val mimeTypeValidationResult = filePart.isValidMimeType(imagesConfig.allowedMimeSubtypes)
        val detectedMimeSubtype = mimeTypeValidationResult.mimeSubtype
        if (!mimeTypeValidationResult.isValid) {
            return ResponseEntity.badRequest().body("File is not an accepted type: $detectedMimeSubtype")
        }
        log.info("File is an accepted type: $detectedMimeSubtype")

        // TODO prevent very large files from being uploaded

        // save the file to the storage service
        val bytes = filePart.toByteArray()
        val savedImageLocation = storageService.saveImage(bytes, detectedMimeSubtype!!)
        log.info("Saved image location: $savedImageLocation")

        // TODO capture actual user id after authentication is implemented
//        val userId = UUID.fromString("1aeabbac-84a5-11ee-9c3b-37b635df60b6")

        // save the image to the database
//        val fileSize = bytes.size.toLong()
//        val image = filePart.toImage(userId, imageLocation, fileSize)
//        val savedImage = imageService.saveImage(image)

        // TODO publish an event to the message broker to perform the image processing

        // TODO return a 202 Accepted response?

        return ResponseEntity.ok("File uploaded successfully")
    }

    @GetMapping("/accepted-types")
    suspend fun getAcceptedImageTypes(): ResponseEntity<AcceptedImageTypes> {
        val acceptedTypeStr = imagesConfig.allowedMimeSubtypes
        val acceptedTypes = AcceptedImageTypes(acceptedTypeStr)
        return ResponseEntity.ok(acceptedTypes)
    }

    @GetMapping
    suspend fun getAllImages(): ResponseEntity<Flow<Image>> {

        // TODO get all images for the current user instead of all images

        val images = imageService.findAll()
        return ResponseEntity.ok(images)
    }

    @GetMapping("/{id}")
    suspend fun getImageById(@PathVariable id: UUID): ResponseEntity<Image> {
        val image = imageService.findById(id)
        return if (image != null) ResponseEntity.ok(image)
        else ResponseEntity.notFound().build()
    }

    /**
     * NOTE: Normally, we would use a @ControllerAdvice class to handle exceptions but this is a small app and probably overkill.
     */
    /* @ExceptionHandler(
         MethodArgumentTypeMismatchException::class,
         IllegalArgumentException::class
     )
     suspend fun handleValidationExceptions(ex: Throwable): ResponseStatusException {
         return ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
     }*/
}