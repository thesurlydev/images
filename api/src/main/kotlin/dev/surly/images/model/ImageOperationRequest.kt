package dev.surly.images.model

import java.util.UUID

data class ImageOperationRequest(val userId: UUID,
                                 val operationId: UUID,
                                 val imageId: UUID)
