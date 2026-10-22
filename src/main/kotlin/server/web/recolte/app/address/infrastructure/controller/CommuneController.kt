package server.web.recolte.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.address.application.service.CommuneService
import server.web.recolte.app.address.domain.model.Commune
import server.web.recolte.app.address.domain.model.request.CommuneRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.address.CommuneScope

@Tag(name = "Commune", description = "Gestion des communes")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class CommuneController(
    private val service: CommuneService,
) {
    @PostMapping(
        "/{version}/${CommuneScope.PROTECTED}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun create(
        @PathVariable version: String,
        @Valid @RequestBody request: CommuneRequest,
    ): ResponseEntity<Map<String, Any>> {
        val commune = service.create(request)
        return ResponseEntity.status(201).body(
            mapOf(
                "commune" to commune,
                "message" to "Enregistrement réussie avec succès",
            ),
        )
    }

    @GetMapping("/{version}/${CommuneScope.PUBLIC}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findAll(@PathVariable version: String): ResponseEntity<Map<String, List<Commune>>> =
        ResponseEntity.ok(mapOf("communes" to service.findAll()))

    @GetMapping("/{version}/${CommuneScope.PUBLIC}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findById(
        @PathVariable version: String,
        @PathVariable id: Long,
    ): ResponseEntity<Map<String, Commune>> =
        ResponseEntity.ok(mapOf("commune" to service.findById(id)))
}
