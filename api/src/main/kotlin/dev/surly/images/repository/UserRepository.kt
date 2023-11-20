package dev.surly.images.repository

import dev.surly.images.model.Image
import dev.surly.images.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun findByUsername(username: String): User
}