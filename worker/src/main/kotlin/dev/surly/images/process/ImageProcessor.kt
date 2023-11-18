package dev.surly.images.process

import java.awt.Image
import java.awt.geom.AffineTransform
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

class ImageProcessor {

    fun transformImage(input: ByteArray, formatName: String): ByteArray {
        // Read the image from the file
        val originalImage: BufferedImage = input.toBufferedImage()
        val transformedImage = BufferedImage(originalImage.width, originalImage.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = transformedImage.createGraphics()

        // Create an AffineTransform object to chain operations
        val at = AffineTransform()

        // Translate, rotate, then scale
        at.translate(originalImage.width / 2.0, originalImage.height / 2.0)

        // TODO
        at.rotate(Math.toRadians(90.0))  // Rotate 90 degrees
        at.scale(1.5, 1.5)  // Scale up by 1.5 times
        at.translate(-originalImage.width / 2.0, -originalImage.height / 2.0)

        // Apply the transformations to the Graphics2D object
        g2d.transform = at

        // Draw the transformed image
        g2d.drawImage(originalImage, 0, 0, null)
        g2d.dispose()

        // Write the transformed image to a byte array output stream
        val baos = ByteArrayOutputStream()
        ImageIO.write(transformedImage, "PNG", baos)
        baos.flush()
        val imageInByte: ByteArray = baos.toByteArray()
        baos.close()

        return imageInByte
    }


    fun resize(image: ByteArray, width: Int, height: Int): ByteArray {

        val originalImage: BufferedImage = image.toBufferedImage()
        val scaledImage: Image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)

        val bImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = bImage.createGraphics()
        g.drawImage(scaledImage, 0, 0, null)
        g.dispose()

        // write bImage to outputstream
        return bImage.toByteArray("jpg")
    }

    fun rotate(image: ByteArray, degrees: Double): ByteArray {
        val img: BufferedImage = image.toBufferedImage()

        val width = img.width
        val height = img.height

        val rotatedImage = BufferedImage(width, height, img.type)

        val theta = Math.toRadians(degrees)
        val x = (width / 2).toDouble()
        val y = (height / 2).toDouble()

        val g = rotatedImage.createGraphics()
        g.rotate(theta, x, y)
        g.drawImage(img, null, 0, 0)
        g.dispose()

        return rotatedImage.toByteArray("jpg")
    }
}