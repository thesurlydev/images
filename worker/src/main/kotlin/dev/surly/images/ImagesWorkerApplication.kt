package dev.surly.images

import dev.surly.images.process.ImageProcessor
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File
import javax.imageio.ImageIO


@SpringBootApplication
class ImagesWorkerApplication

fun main(args: Array<String>) {
//	runApplication<ImagesWorkerApplication>(*args)

    /*val testImage = File("/home/shane/projects/images/api/uploads/test1-360k.jpg")
    val testImageBytes = testImage.readBytes()

    val proc = ImageProcessor()
    proc.resize(testImageBytes, 1280, 720)*/
//    proc.rotate(testImage.readBytes(), 180.0)


    ImageIO.getWriterFormatNames().forEach { println(it) }
}


