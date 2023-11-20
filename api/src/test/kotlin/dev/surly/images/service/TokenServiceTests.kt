package dev.surly.images.service

import dev.surly.images.model.VerifyTokenResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenServiceTests {

    @Autowired private lateinit var tokenService: TokenService

    @Test
    fun `create token and verify its valid`() {
        // given
        val encodedSecretKey = "kD7DPj/2s+8grue2xa6Od9mWbBH8FmF0hnTFf6bVVq8="
        val decodedSecretKey = Base64.getDecoder().decode(encodedSecretKey)

        val testId = UUID.fromString("1aeabbac-84a5-11ee-9c3b-37b635df60b6")
        val token = tokenService.createJwtToken(testId, decodedSecretKey)
        println("Token: $token")

        // when
        val result: VerifyTokenResult = tokenService.verifyToken(token, decodedSecretKey)

        // then
        assert(result.isValid) { "Token should be valid" }
        val actualSubject = result.userId ?: fail { "Token subject should not be null" }
        assert(actualSubject == testId) { "Token subject should be 'test'" }
    }

    @Test
    fun `create expired token and verify token is NOT valid`() {
        // given
        val encodedSecretKey = "kD7DPj/2s+8grue2xa6Od9mWbBH8FmF0hnTFf6bVVq8="
        val decodedSecretKey = Base64.getDecoder().decode(encodedSecretKey)

        val testId = UUID.fromString("1aeabbac-84a5-11ee-9c3b-37b635df60b6")
        val token = tokenService.createJwtToken(testId, decodedSecretKey, 0)
        println("Token: $token")

        // when
        val result: VerifyTokenResult = tokenService.verifyToken(token, decodedSecretKey)

        // then
        assert(!result.isValid) { "Token should be invalid because it's expired" }
    }

    @Test
    fun `verify a bogus token is NOT valid`() {
        // given
        val encodedSecretKey = "kD7DPj/2s+8grue2xa6Od9mWbBH8FmF0hnTFf6bVVq8="
        val decodedSecretKey = Base64.getDecoder().decode(encodedSecretKey)

        val token = "bogustoken"

        // when
        val result: VerifyTokenResult = tokenService.verifyToken(token, decodedSecretKey)

        // then
        assert(!result.isValid) { "Token should be invalid" }
    }
}