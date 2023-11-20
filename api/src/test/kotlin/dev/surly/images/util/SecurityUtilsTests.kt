package dev.surly.images.util

import org.junit.jupiter.api.Test
import java.util.*

class SecurityUtilsTests {

    @Test
    fun `generate HmacSHA256 key`() {
        // given
        val secretKey = SecurityUtils.generateHmacShaKey()
        // when
        val encodedKey = Base64.getEncoder().encodeToString(secretKey.encoded)
        // then
        assert(encodedKey.isNotEmpty())
    }

    @Test
    fun `verifyPassword returns true when password is valid`() {
        // given
        val password = "password"

        // when
        val hash = password.hashPassword();
        val verified = SecurityUtils.verifyPassword(password, hash)

        // then
        assert(verified) { "verifyPassword should return true" }
    }

    @Test
    fun `verifyPassword returns false when password is invalid`() {
        // given
        val password = "password"

        // when
        val hash = password.hashPassword();
        val bogusPass = "foo"
        val verified = SecurityUtils.verifyPassword(bogusPass, hash)

        // then
        assert(!verified) { "verifyPassword should return false" }
    }
}