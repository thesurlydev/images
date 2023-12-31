package dev.surly.images.model

data class SaveImageResult(
    val location: String,
    val sizeInBytes: Long,
    val dimensions: Pair<Int, Int>
)
