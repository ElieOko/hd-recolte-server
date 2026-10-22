package server.web.recolte.app.promotion.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.promotion.application.service.PromotionService
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.promotion.PromotionScope

@Tag(name = "Promotion", description = "Promotions")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class PromotionController(
    private val service: PromotionService,
) {
    @GetMapping("/{version}/${PromotionScope.PUBLIC}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun index(@PathVariable version: String): ResponseEntity<Map<String, Any>> =
        ResponseEntity.ok(mapOf("promotions" to service.findAll()))

    @PostMapping("/{version}/${PromotionScope.PROTECTED}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun store(@PathVariable version: String): ResponseEntity<Map<String, Any>> =
        ResponseEntity.status(201).body(
            mapOf(
                "promotion" to service.create(),
                "message" to "Enregistrement réussie avec succès",
            ),
        )
}
