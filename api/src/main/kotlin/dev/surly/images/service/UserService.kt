package dev.surly.images.service

import dev.surly.images.model.User
import dev.surly.images.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(val userRepository: UserRepository) {
    suspend fun findById(id: UUID): User? = userRepository.findById(id)
    suspend fun findAll(): Flow<User> = userRepository.findAll()
}