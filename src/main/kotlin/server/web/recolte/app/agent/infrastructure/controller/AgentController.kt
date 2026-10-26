package server.web.recolte.app.agent.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.agent.application.service.AgentService
import server.web.recolte.app.agent.domain.model.request.AgentAuthRequest
import server.web.recolte.app.agent.domain.model.request.AgentStoreRequest
import server.web.recolte.app.client.application.service.ClientService
import server.web.recolte.app.trash.application.service.ClientTrashService
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.agent.AgentScope
import server.web.recolte.utils.FieldRules

@Tag(name = "Agent", description = "Comptes agents de collecte")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class AgentController(
    private val service: AgentService,
    private val clients: ClientService,
    private val trashes: ClientTrashService,
) {
    @GetMapping(
        AgentScope.RESOURCE,
        "/{version}/${AgentScope.PUBLIC}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun index(): ResponseEntity<Map<String, Any>> {
        val agents = service.findAll()
        if (agents.isEmpty()) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Ressource not found"))
        }
        return ResponseEntity.ok(mapOf("agents" to agents))
    }

    @PostMapping(
        AgentScope.RESOURCE,
        "/{version}/${AgentScope.PUBLIC}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun store(@RequestBody request: AgentStoreRequest): ResponseEntity<Map<String, Any>> {
        FieldRules.required(
            mapOf(
                "nom" to request.nom,
                "prenom" to request.prenom,
                "telephone" to request.telephone,
            ),
            HttpStatus.BAD_REQUEST,
        )
        val agent = service.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "agent" to agent,
                "message" to "Compte Agent créer avec succès",
            ),
        )
    }

    @PostMapping(
        AgentScope.AUTH,
        "/{version}/${AgentScope.PUBLIC}/auth",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun auth(@RequestBody request: AgentAuthRequest): ResponseEntity<Map<String, Any>> {
        FieldRules.required(mapOf("code" to request.code), HttpStatus.PAYMENT_REQUIRED)
        val agent = service.authenticate(request.code!!)
        return ResponseEntity.ok(
            mapOf(
                "message" to "login success",
                "agent" to agent,
                "client" to clients.findAll(),
                "trash_client" to trashes.findAll(),
            ),
        )
    }
}
