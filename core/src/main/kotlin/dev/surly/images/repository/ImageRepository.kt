package dev.surly.images.repository

import dev.surly.images.model.Image
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface ImageRepository : CoroutineCrudRepository<Image, UUID> {
    fun findByUserId(userId: UUID): Flow<Image>

    suspend fun findByUserIdAndId(userId: UUID, id: UUID): Image?
}