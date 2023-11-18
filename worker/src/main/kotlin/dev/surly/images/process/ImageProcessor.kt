package dev.surly.images.process

import dev.surly.images.model.ImageTransformRequest
import dev.surly.images.util.toBufferedImage
import dev.surly.images.util.toByteArray
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.stream.Collectors

@Component
class ImageProcessor {

    companion object {
        private val log = LoggerFactory.getLogger(ImageProcessor::class.java)
    }
    fun transformImage(input: ByteArray, formatName: String, itr: ImageTransformRequest): ByteArray {
        var transformedImage = input
        for (transform in itr.transforms) {
            log.info("PROC: Applying transform: ${transform.type}")
            transformedImage = transform.apply(transformedImage, formatName)
        }
        return transformedImage
    }
}