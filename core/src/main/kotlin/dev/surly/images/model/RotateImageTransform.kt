package dev.surly.images.model

import com.fasterxml.jackson.annotation.JsonTypeName
import dev.surly.images.util.toBufferedImage
import dev.surly.images.util.toByteArray
import java.awt.image.BufferedImage

@JsonTypeName("Rotate")
class RotateImageTransform(val degrees: Double) : ImageTransform {

    override val type: String = "Rotate"

    override fun apply(image: ByteArray, formatName: String): ByteArray {
        val img: BufferedImage = image.toBufferedImage()

        val width = img.width
        val height = img.height

        val rotatedImage = BufferedImage(width, height, img.type)

        val theta = Math.toRadians(this.degrees)
        val x = (width / 2).toDouble()
        val y = (height / 2).toDouble()

        val g = rotatedImage.createGraphics()
        g.rotate(theta, x, y)
        g.drawImage(img, null, 0, 0)
        g.dispose()

        return rotatedImage.toByteArray(formatName)
    }
}