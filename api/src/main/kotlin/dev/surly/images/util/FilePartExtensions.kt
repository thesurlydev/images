package dev.surly.images.util

import dev.surly.images.model.Image
import dev.surly.images.model.MimeTypeValidationResult
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
    fun FilePart.toImage(userId: UUID, path: String, size: Long, type: String, width: Int, height: Int): Image = Image(
        userId = userId,
        path = path,
        type = type,
        fileSizeBytes = size,
        status = "processing",
        width = width,
        height = height
    )

    private fun getFileMimeTypeWithTika(filePart: FilePart): Flux<MediaType> {
        val tika = TikaConfig()
        return filePart.content()
            .map { dataBuffer ->
                tika.detector.detect(dataBuffer.asInputStream(), org.apache.tika.metadata.Metadata())
            }
    }

    suspend fun FilePart.isValidMimeType(acceptedMimeTypes: Set<String>): MimeTypeValidationResult {
        val mimeType = this.headers().contentType
        mimeType?.let {
            val mimeTypeStr = it.toString()
            when {
                acceptedMimeTypes.contains(mimeTypeStr) -> return MimeTypeValidationResult(mimeTypeStr, true)
                else -> {
                    // let's try Tika
                    val tikaMediaType = getFileMimeTypeWithTika(this).awaitFirstOrNull()
                    val tikaMimeTypeStr = tikaMediaType.toString()
                    when {
                        acceptedMimeTypes.contains(tikaMimeTypeStr) -> return MimeTypeValidationResult(tikaMimeTypeStr, true)
                        else -> return MimeTypeValidationResult(tikaMimeTypeStr, false)
                    }
                }
            }
        }
        return MimeTypeValidationResult(null, false)
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