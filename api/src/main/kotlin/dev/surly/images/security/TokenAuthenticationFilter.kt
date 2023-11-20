package dev.surly.images.security

import dev.surly.images.config.AuthConfig
import dev.surly.images.util.SecurityUtils.Companion.verifyToken
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.*


@Component
class TokenAuthenticationFilter(val authConfig: AuthConfig) : WebFilter {

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)
    }

    private final val AUTHORIZATION_HEADER_NAME = "Authorization"

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {

        val requestPath = exchange.request.uri.path
        val requestMethod = exchange.request.method.name()

        // log request
        if (authConfig.logRequests) {
            log.info(">> {} {}", requestMethod, requestPath)
        }

        // if auth is disabled, skip authentication
        if (!authConfig.enabled) {
            log.info("Auth is disabled; skipping authentication")
            return chain.filter(exchange)
        }

        // if request path is in excluded paths, skip authentication
        val excludedPaths = authConfig.excludedPaths
        if (excludedPaths.any { path -> path == requestPath || matchPathPattern(path, requestPath) }) {
            log.info("Request path is in excluded paths; skipping authentication")
            return chain.filter(exchange)
        }

        // extract token from request header
        val maybeToken = exchange.request.headers.getFirst(AUTHORIZATION_HEADER_NAME)
        if (maybeToken.isNullOrEmpty()) {
            log.info("No token found in request header")
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }

        val token = maybeToken.substringAfter("Bearer ")

        return mono {
            val secretKey = Base64.getDecoder().decode(authConfig.secretKey)
            val verificationResult = verifyToken(token, secretKey)
            if (verificationResult.isValid) {
                // store username for downstream processing
                exchange.attributes["userId"] = verificationResult.userId
                chain.filter(exchange).awaitSingleOrNull()
            } else {
                log.warn("Token verification failed for token: $token")
                exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                null
            }
        }.onErrorResume {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            exchange.response.setComplete()
        }
    }

    private fun matchPathPattern(pattern: String, path: String): Boolean {
        val strippedPattern = pattern.removeSuffix("**")
        return path.startsWith(strippedPattern)
    }
}