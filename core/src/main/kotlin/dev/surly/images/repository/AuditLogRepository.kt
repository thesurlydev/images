package dev.surly.images.repository

import dev.surly.images.model.AuditLog
import dev.surly.images.model.Image
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface AuditLogRepository: CoroutineCrudRepository<AuditLog, UUID> {
    fun findByUserId(userId: UUID): Flow<AuditLog>
}