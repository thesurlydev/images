package dev.surly.images.service

import dev.surly.images.model.Image
import dev.surly.images.model.Operation
import dev.surly.images.repository.ImageRepository
import dev.surly.images.repository.OperationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class ImageService(
    val imageRepository: ImageRepository,
    val operationRepository: OperationRepository
) {
//    suspend fun findById(id: UUID): Image? = imageRepository.findById(id)
    suspend fun findByUserId(id: UUID): Flow<Image> = imageRepository.findByUserId(id)
    suspend fun findByUserIdAndId(userId: UUID, id: UUID): Image? = imageRepository.findByUserIdAndId(userId, id)
    suspend fun saveImage(image: Image): Image = imageRepository.save(image)
    suspend fun findAllOperations(): Flow<Operation> = operationRepository.findAll()

}