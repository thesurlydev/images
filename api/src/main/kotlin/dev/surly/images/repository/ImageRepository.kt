package dev.surly.images.repository

import dev.surly.images.model.Image
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageRepository : CoroutineCrudRepository<Image, UUID> {}