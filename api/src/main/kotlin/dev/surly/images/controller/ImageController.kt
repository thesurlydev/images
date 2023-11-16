package dev.surly.images.controller

import dev.surly.images.model.Image
import dev.surly.images.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/images")
class ImageController(private val imageService: ImageService) {

    @GetMapping("/{id}")
    suspend fun getImageById(@PathVariable id: UUID): ResponseEntity<Image> {
        val image = imageService.findById(id)
        return if (image != null) ResponseEntity.ok(image)
        else ResponseEntity.notFound().build()
    }

    /**
     * NOTE: Normally, we would use a @ControllerAdvice class to handle exceptions but this is a small app and probably overkill.
     */
    @ExceptionHandler(
        MethodArgumentTypeMismatchException::class,
        IllegalArgumentException::class
    )
    fun handleValidationExceptions(ex: Throwable): ResponseStatusException {
        return ResponseStatusException(HttpStatus.BAD_REQUEST, ex.message, ex)
    }
}