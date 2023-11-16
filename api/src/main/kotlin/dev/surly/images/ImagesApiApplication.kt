package dev.surly.images

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ImagesApiApplication

fun main(args: Array<String>) {
    runApplication<ImagesApiApplication>(*args)
}
