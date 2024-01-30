package dev.surly.images

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan("dev.surly.images.config")
class ImagesApiApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ImagesApiApplication>(*args)
        }
    }
}

