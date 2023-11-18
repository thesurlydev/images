package dev.surly.images

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("dev.surly.images.config")
class ImagesWorkerApplication

fun main(args: Array<String>) {
    runApplication<ImagesWorkerApplication>(*args)

    /*val testImage = File("/home/shane/projects/images/api/uploads/test1-360k.jpg")
    val testImageBytes = testImage.readBytes()

    val proc = ImageProcessor()
    proc.resize(testImageBytes, 1280, 720)*/
//    proc.rotate(testImage.readBytes(), 180.0)


//    ImageIO.getWriterFormatNames().forEach { println(it) }

}


