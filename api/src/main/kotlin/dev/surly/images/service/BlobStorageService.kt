package dev.surly.images.service

import java.awt.image.RenderedImage
import java.nio.file.Path

interface BlobStorageService {
    suspend fun saveImage(image: RenderedImage, path: Path)
    suspend fun loadImage(path: Path): RenderedImage
}