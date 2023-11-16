package dev.surly.images.service

import dev.surly.images.model.Image
import dev.surly.images.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageService(val imageRepository: ImageRepository) {
    suspend fun findById(id: UUID): Image? = imageRepository.findById(id)
    suspend fun findAll(): Flow<Image> = imageRepository.findAll()
}