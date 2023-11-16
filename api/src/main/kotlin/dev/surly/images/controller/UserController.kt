package dev.surly.images.controller

import dev.surly.images.model.User
import dev.surly.images.service.UserService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    suspend fun getUserById(): ResponseEntity<Flow<User>> {
        val users = userService.findAll()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: UUID): ResponseEntity<User> {
        val user = userService.findById(id)
        return if (user != null) ResponseEntity.ok(user)
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
        return ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, ex.message, ex)
    }
}