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
import server.web.recolte.app.address.application.service.DistrictService
import server.web.recolte.app.address.domain.model.District
import server.web.recolte.app.address.domain.model.request.DistrictRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.address.DistrictScope

@Tag(name = "District", description = "Gestion des districts")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class DistrictController(
    private val service: DistrictService,
) {
    @PostMapping(
        "/{version}/${DistrictScope.PROTECTED}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun create(
        @PathVariable version: String,
        @Valid @RequestBody request: DistrictRequest,
    ): ResponseEntity<Map<String, Any>> {
        val district = service.create(request)
        return ResponseEntity.status(201).body(
            mapOf(
                "district" to district,
                "message" to "Enregistrement réussie avec succès",
            ),
        )
    }

    @GetMapping("/{version}/${DistrictScope.PUBLIC}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findAll(@PathVariable version: String): ResponseEntity<Map<String, List<District>>> =
        ResponseEntity.ok(mapOf("districts" to service.findAll()))

    @GetMapping("/{version}/${DistrictScope.PUBLIC}/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun findById(
        @PathVariable version: String,
        @PathVariable id: Long,
    ): ResponseEntity<Map<String, District>> =
        ResponseEntity.ok(mapOf("district" to service.findById(id)))
}
