package server.web.recolte.app.client.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.client.application.service.ClientService
import server.web.recolte.app.client.domain.model.request.ClientAuthRequest
import server.web.recolte.app.client.domain.model.request.ClientStoreRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.client.ClientScope
import server.web.recolte.utils.FieldRules

@Tag(name = "Client", description = "Comptes clients et authentification par téléphone")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class ClientController(
    private val service: ClientService,
) {
    @GetMapping(
        ClientScope.RESOURCE,
        "/{version}/${ClientScope.PUBLIC}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun index(): ResponseEntity<Map<String, Any>> {
        val clients = service.findAll()
        if (clients.isEmpty()) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Ressource not found"))
        }
        return ResponseEntity.ok(mapOf("clients" to clients))
    }

    @PostMapping(
        ClientScope.RESOURCE,
        "/{version}/${ClientScope.PUBLIC}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun store(@RequestBody request: ClientStoreRequest): ResponseEntity<Map<String, Any>> {
        FieldRules.required(
            mapOf(
                "nom" to request.nom,
                "prenom" to request.prenom,
                "telephone" to request.telephone,
                "avenue" to request.avenue,
                "quartier" to request.quartier,
            ),
            HttpStatus.BAD_REQUEST,
        )
        val address = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "client" to address,
                "message" to "Compte client créer avec succès",
            ),
        )
    }

    @PostMapping(
        ClientScope.AUTH,
        "/{version}/${ClientScope.PUBLIC}/auth",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun auth(@RequestBody request: ClientAuthRequest): ResponseEntity<Map<String, Any>> {
        FieldRules.required(
            mapOf(
                "telephone" to request.telephone,
                "avenue" to request.avenue,
            ),
            HttpStatus.PAYMENT_REQUIRED,
        )
        val client = service.authenticate(request.telephone!!, request.avenue!!)
        return ResponseEntity.ok(
            mapOf(
                "message" to "login success",
                "client" to client,
            ),
        )
    }
}
