package dev.surly.images.util

object MimeTypeExtensions {
    fun String.toExtension(): String {
        this.split("/").let {
            return it[it.size - 1]
        }
    }
}