package dev.surly.images.util

import dev.surly.images.model.VerifyTokenResult
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.*

class SecurityUtilsTests {

    @Test
    fun `generate HmacSHA256 key`() {
        val secretKey = SecurityUtils.generateHmacShaKey()
        val encodedKey = Base64.getEncoder().encodeToString(secretKey.encoded)
        println(encodedKey)
    }

    @Test
    fun `create token and verify`() {
        val encodedSecretKey = "kD7DPj/2s+8grue2xa6Od9mWbBH8FmF0hnTFf6bVVq8="
        val decodedSecretKey = Base64.getDecoder().decode(encodedSecretKey)

//        val encodedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxYWVhYmJhYy04NGE1LTExZWUtOWMzYi0zN2I2MzVkZjYwYjYiLCJpYXQiOjE3MDA0OTQ5NzUsImV4cCI6MTcwMDQ5ODU3NX0.oncsnmxq4JTMzPgoccpM1nkyS2U-XUflL7zsb0Ex3eI"
//        val token = Base64.getDecoder().decode(encodedToken)

        val testId = UUID.fromString("1aeabbac-84a5-11ee-9c3b-37b635df60b6")
        val token = SecurityUtils.createJwtToken(testId, decodedSecretKey)
        println("Token: $token")
        val result: VerifyTokenResult = SecurityUtils.verifyToken(token, decodedSecretKey)
        assert(result.isValid) { "Token should be verified" }
        val actualSubject = result.userId ?: fail { "Token subject should not be null" }
        assert(actualSubject == testId) { "Token subject should be 'test'" }
    }

    @Test
    fun `verifyPassword returns true when password matches hash`() {
        val password = "password"
        val hash = password.hashPassword();
        println("Hash: $hash")
        val verified = SecurityUtils.verifyPassword(password, hash)
        assert(verified)
    }

    @Test
    fun `verifyPassword returns false when password does not match hash`() {
        val password = "password"
        val hash = password.hashPassword();
        val bogusPass = "foo"
        val verified = SecurityUtils.verifyPassword(bogusPass, hash)
        assert(!verified)
    }
}