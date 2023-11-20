package dev.surly.images.controller

import dev.surly.images.model.Operation
import dev.surly.images.service.ImageService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OperationController(private val imageService: ImageService) {
    @GetMapping("/api/operations")
    suspend fun operations(): ResponseEntity<Flow<Operation>> {
        val ops = imageService.findAllOperations();
        return ResponseEntity.ok(ops);
    }
}