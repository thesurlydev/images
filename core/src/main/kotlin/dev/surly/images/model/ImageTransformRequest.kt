package dev.surly.images.model

import java.util.*

data class ImageTransformRequest(val imageId: UUID,
                                 val userId: UUID,
                                 val transforms: List<ImageTransform>) {
}