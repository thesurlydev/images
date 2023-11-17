package dev.surly.images.util

import dev.surly.images.model.Image
import dev.surly.images.model.MediaTypeValidationResult
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import org.apache.tika.config.TikaConfig
import org.apache.tika.mime.MediaType
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.util.*

object FilePartExtensions {

    /**
     * Convert a FilePart to an Image object.
     */
    fun FilePart.toImage(userId: UUID, path: String, size: Long): Image = Image(
        userId = userId,
        path = path,
        type = this.headers().contentType.toString(),
        fileSizeBytes = size,
        status = "processing",
    )

    private fun getFileMimeTypeWithTika(filePart: FilePart): Flux<MediaType> {
        val tika = TikaConfig()
        return filePart.content()
            .map { dataBuffer ->
                tika.detector.detect(dataBuffer.asInputStream(), org.apache.tika.metadata.Metadata())
            }
    }

    suspend fun FilePart.isValidMediaType(acceptedImageTypes: Set<String>): MediaTypeValidationResult {
        val mediaType = this.headers().contentType
        mediaType?.let {
            val type = it.type
            val subType = it.subtype
            if (type == "image" && acceptedImageTypes.contains(subType)) {
                return MediaTypeValidationResult(mediaType.toString(), isValid = true)
            } else {
                // let's try Tika
                val tikaMediaType = getFileMimeTypeWithTika(this).awaitFirstOrNull()
                if (tikaMediaType?.type == "image" && acceptedImageTypes.contains(tikaMediaType.subtype)) {
                    return MediaTypeValidationResult(tikaMediaType.toString(), true)
                } else {
                    return MediaTypeValidationResult(tikaMediaType.toString(), false)
                }
            }
        }
        return MediaTypeValidationResult(mediaType.toString(), false)
    }

    suspend fun FilePart.toByteArray(): ByteArray {
        return this.content()
            .reduce(ByteArrayOutputStream()) { baos, dataBuffer ->
                dataBuffer.asInputStream().copyTo(baos)
                baos
            }
            .map { it.toByteArray() }
            .awaitSingle()
    }
}