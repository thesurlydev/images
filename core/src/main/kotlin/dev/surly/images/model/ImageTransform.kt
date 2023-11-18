package dev.surly.images.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = RotateImageTransform::class, name = "Rotate"),
    JsonSubTypes.Type(value = ScaleImageTransform::class, name = "Scale")
)
interface ImageTransform {
    val type: String
    fun apply(image: ByteArray, formatName: String): ByteArray
}