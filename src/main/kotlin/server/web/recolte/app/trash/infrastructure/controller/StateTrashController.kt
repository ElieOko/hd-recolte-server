package server.web.recolte.app.trash.infrastructure.controller

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
import server.web.recolte.app.trash.application.service.StateTrashService
import server.web.recolte.app.trash.domain.model.request.StateTrashRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.trash.StateTrashScope

@Tag(name = "Etat poubelle", description = "Référentiel des états de poubelle")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class StateTrashController(
    private val service: StateTrashService,
) {
    @GetMapping("/{version}/${StateTrashScope.PUBLIC}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun index(@PathVariable version: String): ResponseEntity<Map<String, Any>> =
        ResponseEntity.ok(mapOf("state_trashes" to service.findAll()))

    @PostMapping(
        "/{version}/${StateTrashScope.PROTECTED}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun store(
        @PathVariable version: String,
        @Valid @RequestBody request: StateTrashRequest,
    ): ResponseEntity<Map<String, Any>> =
        ResponseEntity.status(201).body(
            mapOf(
                "state_trash" to service.create(request),
                "message" to "Enregistrement réussie avec succès",
            ),
        )
}
