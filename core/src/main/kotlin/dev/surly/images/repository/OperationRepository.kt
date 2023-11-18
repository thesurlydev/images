package dev.surly.images.repository

import dev.surly.images.model.Operation
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OperationRepository : CoroutineCrudRepository<Operation, UUID> {
}