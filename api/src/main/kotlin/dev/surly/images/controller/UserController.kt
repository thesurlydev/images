package dev.surly.images.controller

import dev.surly.images.config.AuthConfig
import dev.surly.images.model.*
import dev.surly.images.service.TokenService
import dev.surly.images.service.UserService
import dev.surly.images.util.SecurityUtils.Companion.verifyPassword
import org.springframework.dao.DuplicateKeyException
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
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(UserController::class.java)
    }

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

    @PostMapping("/login")
    suspend fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val user = userService.findByUsername(loginRequest.username)
        return when {
            user != null -> {
                val verified = verifyPassword(loginRequest.password, user.passwordHash)
                when {
                    verified -> {
                        val decodedSecretKey = Base64.getDecoder().decode(authConfig.secretKey)
                        val token = tokenService.createJwtToken(user.id!!, decodedSecretKey)
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


    @PostMapping("/register")
    suspend fun login(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<Any> {
        try {
            val user = registrationRequest.toUser()
            val createdUser = userService.create(user)
            return createdUser.toWhoAmIResponse().let { whoAmIResponse ->
                ResponseEntity.status(HttpStatus.CREATED).body(whoAmIResponse)
            }
        } catch (dke: DuplicateKeyException) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                    mapOf(
                        "status" to "Conflict",
                        "message" to "Username or email already exists"
                    )
                )
        } catch (e: Exception) {
            log.error("Error creating user", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    mapOf(
                        "status" to "Error",
                        "message" to "Internal Server Error"
                    )
                )
        }
    }
}

