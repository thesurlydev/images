package dev.surly.images.controller

import dev.surly.images.config.AuthConfig
import dev.surly.images.model.User
import dev.surly.images.service.UserService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api")
class UserController(
    private val userService: UserService,
    val authConfig: AuthConfig,
) {

    @GetMapping("/whoami")
    suspend fun getUserById(): ResponseEntity<User> {
        val user = userService.findById(authConfig.testUserId)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }
}