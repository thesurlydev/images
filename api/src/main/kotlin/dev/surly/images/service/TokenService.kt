package dev.surly.images.service

import dev.surly.images.config.AuthConfig
import dev.surly.images.model.VerifyTokenResult
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(val authConfig: AuthConfig) {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TokenService::class.java)
    }
    fun createJwtToken(userId: UUID, secretKey: ByteArray, expiry: Long? = null): String {
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        val tokenExpiry = expiry ?: authConfig.tokenExpiry
        val expMillis = nowMillis + tokenExpiry
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
}