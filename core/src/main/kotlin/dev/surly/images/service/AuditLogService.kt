package dev.surly.images.service

import dev.surly.images.model.AuditLog
import dev.surly.images.repository.AuditLogRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuditLogService(val auditLogRepository: AuditLogRepository) {

    suspend fun logSuccess(userId: UUID, operation: String, message: String) {
        AuditLog(
            userId = userId,
            operation = operation,
            message = message,
            status = "SUCCESS"
        ).let { auditLogRepository.save(it) }
    }
    suspend fun logError(userId: UUID, operation: String, message: String) {
        AuditLog(
            userId = userId,
            operation = operation,
            message = message,
            status = "ERROR"
        ).let { auditLogRepository.save(it) }
    }

    suspend fun findByUser(userId: UUID): Flow<AuditLog> = auditLogRepository.findByUserId(userId)
}