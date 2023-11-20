package dev.surly.images.controller

import dev.surly.images.model.AuditLog
import dev.surly.images.service.AuditLogService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api")
class LogController(
    val auditLogService: AuditLogService
) {

    @GetMapping("/logs")
    suspend fun getLogs(exchange: ServerWebExchange): ResponseEntity<Flow<AuditLog>> {
        val userId = exchange.getAttribute<java.util.UUID>("userId")!!
        val logs = auditLogService.findByUserId(userId)
        return ResponseEntity.ok(logs)
    }
}