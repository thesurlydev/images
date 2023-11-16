package dev.surly.images.controller

import dev.surly.images.config.ImagesConfig
import dev.surly.images.model.Image
import dev.surly.images.service.ImageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories

@RestController
@RequestMapping("/api/images")
class ImageController(private val imageService: ImageService,
                      private val imagesConfig: ImagesConfig
) {
    private val log = LoggerFactory.getLogger(ImageController::class.java)

    @PostMapping("/upload")
    suspend fun uploadImage(@RequestPart("file") filePart: FilePart): ResponseEntity<String> {

        val dir = Path.of("uploads")
        dir.createDirectories()

        val filePath: Path = dir.resolve(filePart.filename())
        log.info("Saving file to: $filePath")
        filePart.transferTo(filePath).awaitSingleOrNull()

        return ResponseEntity.ok("File uploaded successfully")
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