package dev.surly.images.util

import at.favre.lib.crypto.bcrypt.BCrypt
import dev.surly.images.model.VerifyTokenResult
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class SecurityUtils {

    companion object {

        private val log = org.slf4j.LoggerFactory.getLogger(SecurityUtils::class.java)
        fun verifyPassword(password: String, hash: String): Boolean {
            val result = BCrypt.verifyer().verify(password.toCharArray(), hash)
            return result.verified
        }

        fun generateHmacShaKey(): SecretKey {
            val keyGen = KeyGenerator.getInstance("HmacSHA256")
            keyGen.init(256) // Initialize with a key size of 256 bits (32 bytes)
            return keyGen.generateKey()
        }
    }
}

/**
 * Extension function converts a String to a bcrypt hash
 */
fun String.hashPassword(): String {
    val bcryptHashString = BCrypt.withDefaults().hashToString(12, this.toCharArray())
    return bcryptHashString
}