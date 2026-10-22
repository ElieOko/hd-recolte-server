package server.web.recolte.app.trash.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import server.web.recolte.app.client.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.client.infrastructure.persistence.repository.AddressClientRepository
import server.web.recolte.app.client.infrastructure.persistence.repository.ClientRepository
import server.web.recolte.app.trash.domain.model.ClientTrash
import server.web.recolte.app.trash.domain.model.StateTrash
import server.web.recolte.app.trash.domain.model.request.ClientTrashRequest
import server.web.recolte.app.trash.domain.model.request.StateTrashRequest
import server.web.recolte.app.trash.infrastructure.persistence.entity.ClientTrashEntity
import server.web.recolte.app.trash.infrastructure.persistence.entity.StateTrashEntity
import server.web.recolte.app.trash.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.trash.infrastructure.persistence.repository.ClientTrashRepository
import server.web.recolte.app.trash.infrastructure.persistence.repository.StateTrashRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.utils.TimeFormat

@Service
class StateTrashService(
    private val repository: StateTrashRepository,
) {
    suspend fun findAll(): List<StateTrash> = repository.findAllByOrderByIdAsc().map { it.toDomain() }.toList()

    suspend fun create(request: StateTrashRequest): StateTrash {
        val now = TimeFormat.now()
        return repository.save(
            StateTrashEntity(
                niveau = request.niveau ?: 0,
                nom = request.nom.trim(),
                createdAt = now,
                updatedAt = now,
            ),
        ).toDomain()
    }
}

@Service
class ClientTrashService(
    private val trashes: ClientTrashRepository,
    private val states: StateTrashRepository,
    private val clients: ClientRepository,
    private val addresses: AddressClientRepository,
) {
    suspend fun findAll(): List<ClientTrash> =
        trashes.findAllByOrderByIdAsc().toList().map { it.toDomain(loadClient(it.clientId)) }

    suspend fun findByClient(clientId: Long): ClientTrash {
        val trash = trashes.findFirstByClientIdOrderByIdAsc(clientId)
            ?: throw ApiException(
                HttpStatus.NOT_FOUND,
                mapOf("message" to "Votre poubelle n'est pas encore pris en charge cliquez sur Vidage"),
            )
        return trash.toDomain(loadClient(trash.clientId))
    }

    @Transactional
    suspend fun create(request: ClientTrashRequest): ClientTrash {
        val clientId = request.clientId!!
        val stateId = request.stateTrashId!!
        if (clients.findById(clientId) == null) {
            throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "Ce numéro n'est pas identifié"))
        }
        if (states.findById(stateId) == null) {
            throw ApiException(HttpStatus.BAD_REQUEST, mapOf("message" to "Etat de poubelle inexistant"))
        }
        val existing = trashes.findFirstByClientIdAndStateTrashIdAndIsActive(clientId, stateId, true)
        if (existing != null) {
            return existing.toDomain(loadClient(existing.clientId))
        }
        val now = TimeFormat.now()
        val saved = trashes.save(
            ClientTrashEntity(
                clientId = clientId,
                stateTrashId = stateId,
                isActive = true,
                createdAt = now,
                updatedAt = now,
            ),
        )
        return saved.toDomain(loadClient(saved.clientId))
    }

    @Transactional
    suspend fun clean(id: Long, request: ClientTrashRequest): Pair<ClientTrash, String> {
        val clientId = request.clientId!!
        val current = trashes.findFirstByClientIdOrderByIdAsc(clientId)
            ?: throw ApiException(
                HttpStatus.NOT_FOUND,
                mapOf("message" to "Votre poubelle n'est pas encore pris en charge cliquez sur Vidage"),
            )
        val target = trashes.findById(id)
            ?: throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "Poubelle introuvable"))
        val nextState = when (current.stateTrashId) {
            2L -> 1L
            3L -> 2L
            else -> 2L
        }
        val saved = trashes.save(
            target.copy(
                clientId = clientId,
                stateTrashId = nextState,
                isActive = true,
                updatedAt = TimeFormat.now(),
            ),
        )
        val message = if (nextState == 1L) {
            "Votre poubelle est actuellement propre"
        } else {
            "Nettoyage de la poubelle encours"
        }
        return saved.toDomain(loadClient(saved.clientId)) to message
    }

    private suspend fun loadClient(clientId: Long?) =
        clientId?.let { id ->
            val client = clients.findById(id) ?: return@let null
            val linked = addresses.findAllByClientIdOrderByIdAsc(id).toList().map { it.toDomain() }
            client.toDomain(linked)
        }
}
