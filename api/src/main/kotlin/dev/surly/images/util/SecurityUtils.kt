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

        fun createJwtToken(userId: UUID, secretKey: ByteArray): String {
            val nowMillis = System.currentTimeMillis()
            val now = Date(nowMillis)
            val expMillis = nowMillis + 3600000 // The token expiration time, here set to one hour
            val exp = Date(expMillis)
            val key = Keys.hmacShaKeyFor(secretKey)

            return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact()
        }

        fun verifyToken(token: String?, secretKey: ByteArray): VerifyTokenResult {
            if (token.isNullOrEmpty()) return VerifyTokenResult(false)

            // create key from base64 encoded secret key in the config
            val key = Keys.hmacShaKeyFor(secretKey)

            return try {
                // parse the token. if it fails, it will throw an exception, and we'll return false
                // note that this will also fail if the token is expired
                val claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseClaimsJws(token)

                val subject = claims.payload.subject
                VerifyTokenResult(true, UUID.fromString(subject))

            } catch (e: Exception) {
                log.error("Error verifying token: ${e.message}", e)
                VerifyTokenResult(false)
            }
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