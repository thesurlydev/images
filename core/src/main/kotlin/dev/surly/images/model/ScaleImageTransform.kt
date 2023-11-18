package dev.surly.images.model

import com.fasterxml.jackson.annotation.JsonTypeName
import dev.surly.images.util.toBufferedImage
import dev.surly.images.util.toByteArray
import java.awt.Image
import java.awt.image.BufferedImage

@JsonTypeName("Scale")
class ScaleImageTransform(
    val width: Int,
    val height: Int,
) : ImageTransform {
    override val type: String = "Scale"
    override fun apply(image: ByteArray, formatName: String): ByteArray {

        val originalImage: BufferedImage = image.toBufferedImage()
        val scaledImage: Image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)

        val bImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = bImage.createGraphics()
        g.drawImage(scaledImage, 0, 0, null)
        g.dispose()

        return bImage.toByteArray(formatName)
    }
}