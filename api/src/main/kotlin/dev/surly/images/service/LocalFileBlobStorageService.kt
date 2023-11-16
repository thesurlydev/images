package dev.surly.images.service

import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.nio.file.Path
import javax.imageio.ImageIO

class LocalFileBlobStorageService : BlobStorageService {
    override suspend fun saveImage(image: RenderedImage, path: Path) {
        TODO("Not yet implemented")

    }

    override suspend fun loadImage(path: Path): RenderedImage {
        TODO("Not yet implemented")
    }

}