package dev.surly.images

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ImagesWorkerApplication

fun main(args: Array<String>) {
	runApplication<ImagesWorkerApplication>(*args)
}
