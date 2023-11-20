package dev.surly.images.controller

import dev.surly.images.config.AuthConfig
import dev.surly.images.model.AuthenticatedUser
import dev.surly.images.model.LoginRequest
import dev.surly.images.model.User
import dev.surly.images.model.WhoAmIResponse
import dev.surly.images.service.TokenService
import dev.surly.images.service.UserService
import dev.surly.images.util.SecurityUtils.Companion.verifyPassword
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.util.*


@RestController
@RequestMapping()
class UserController(
    private val userService: UserService,
    private val tokenService: TokenService,
    val authConfig: AuthConfig,
) {

    @GetMapping("/whoami")
    suspend fun getUserById(exchange: ServerWebExchange): ResponseEntity<WhoAmIResponse> {
        val userId = exchange.getAttribute<UUID>("userId")!!
        val user = userService.findById(userId)
        return when {
            user != null -> {
                val whoAmIResponse = user.toWhoAmIResponse()
                ResponseEntity.ok(whoAmIResponse)
            }

            else -> ResponseEntity.notFound().build()
        }
    }

    fun User.toWhoAmIResponse(): WhoAmIResponse =
        WhoAmIResponse(this.id, this.username, this.email)

    @PostMapping("/login")
    suspend fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userService.findByUsername(loginRequest.username)
        return when {
            user != null -> {
                val verified = verifyPassword(loginRequest.password, user.passwordHash)
                when {
                    verified -> {
                        val decodedSecretKey = Base64.getDecoder().decode(authConfig.secretKey)
                        val token = tokenService.createJwtToken(user.id, decodedSecretKey)
                        val authUser = AuthenticatedUser(user, token)
                        ResponseEntity.ok(authUser)
                    }

                    else -> return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized: Invalid username or password")
                }
            }

            else -> ResponseEntity.notFound().build()
        }
    }
}