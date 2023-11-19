package dev.surly.images.controller

import dev.surly.images.config.AuthConfig
import dev.surly.images.model.AuditLog
import dev.surly.images.service.AuditLogService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LogController(
    val authConfig: AuthConfig,
    val auditLogService: AuditLogService
) {

    @GetMapping("/logs")
    suspend fun getLogs(): ResponseEntity<Flow<AuditLog>> {
        val logs = auditLogService.findByUser(authConfig.testUserId)
        return ResponseEntity.ok(logs)
    }
}