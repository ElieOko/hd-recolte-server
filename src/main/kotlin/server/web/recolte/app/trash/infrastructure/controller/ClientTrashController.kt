package server.web.recolte.app.trash.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.trash.application.service.ClientTrashService
import server.web.recolte.app.trash.domain.model.request.ClientTrashRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.trash.TrashScope
import server.web.recolte.utils.FieldRules

@Tag(name = "Poubelle", description = "Demandes de vidage et cycle de nettoyage")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class ClientTrashController(
    private val service: ClientTrashService,
) {
    @GetMapping(
        TrashScope.RESOURCE,
        "/{version}/${TrashScope.PUBLIC}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun index(): ResponseEntity<Map<String, Any>> {
        val data = service.findAll()
        if (data.isEmpty()) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Ressource not found"))
        }
        return ResponseEntity.ok(mapOf("trash_clients" to data))
    }

    @PostMapping(
        TrashScope.RESOURCE,
        "/{version}/${TrashScope.PUBLIC}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun store(@RequestBody request: ClientTrashRequest): ResponseEntity<Map<String, Any?>> {
        FieldRules.requiredIds(
            mapOf(
                "client_id" to request.clientId,
                "state_trash_id" to request.stateTrashId,
            ),
            HttpStatus.PAYMENT_REQUIRED,
        )
        val data = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "data" to data,
                "message" to "On arrive pour nettoyer votre poubelle !!",
            ),
        )
    }

    @GetMapping(
        TrashScope.SHOW,
        "/{version}/${TrashScope.PUBLIC}/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun show(@PathVariable id: Long): ResponseEntity<Map<String, Any?>> {
        val data = service.findByClient(id)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "message" to "",
                "data" to data,
            ),
        )
    }

    @PostMapping(
        TrashScope.CLEAN,
        "/{version}/${TrashScope.PUBLIC}/clean/{id}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun clean(
        @PathVariable id: Long,
        @RequestBody request: ClientTrashRequest,
    ): ResponseEntity<Map<String, Any?>> {
        FieldRules.requiredIds(
            mapOf(
                "client_id" to request.clientId,
                "state_trash_id" to request.stateTrashId,
            ),
            HttpStatus.PAYMENT_REQUIRED,
        )
        val (data, message) = service.clean(id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "data" to data,
                "message" to message,
            ),
        )
    }
}
