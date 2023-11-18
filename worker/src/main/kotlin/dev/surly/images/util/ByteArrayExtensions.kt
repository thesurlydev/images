package dev.surly.images.util

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun ByteArray.toBufferedImage(): BufferedImage {
    val img = ImageIO.read(this.inputStream())
    val bImage = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)
    val g = bImage.createGraphics()
    g.drawImage(img, 0, 0, null)
    g.dispose()
    return bImage
}

fun BufferedImage.toByteArray(formatName: String): ByteArray {
    val baos = ByteArrayOutputStream()
    ImageIO.write(this, formatName, baos)
    baos.flush()
    val out: ByteArray = baos.toByteArray().toTypedArray().toByteArray()
    baos.close()
    return out
}