package server.web.recolte.app.client.application.service

import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import server.web.recolte.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.recolte.app.client.domain.model.AddressClient
import server.web.recolte.app.client.domain.model.Client
import server.web.recolte.app.client.domain.model.ClientRegistration
import server.web.recolte.app.client.domain.model.Tranches
import server.web.recolte.app.client.domain.model.request.ClientStoreRequest
import server.web.recolte.app.client.infrastructure.persistence.entity.AddressClientEntity
import server.web.recolte.app.client.infrastructure.persistence.entity.ClientEntity
import server.web.recolte.app.client.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.client.infrastructure.persistence.repository.AddressClientRepository
import server.web.recolte.app.client.infrastructure.persistence.repository.ClientRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.utils.Codes
import server.web.recolte.utils.TimeFormat

@Service
class ClientService(
    private val clients: ClientRepository,
    private val addresses: AddressClientRepository,
    private val communes: CommuneRepository,
) {
    suspend fun findAll(): List<Client> =
        clients.findAllByOrderByIdAsc().toList().map { client ->
            client.toDomain(addressesOf(client.id))
        }

    @Transactional
    suspend fun create(request: ClientStoreRequest): ClientRegistration {
        val souscription = Tranches.require(
            request.montantSouscription,
            request.nombreTranches,
            request.tranche,
        )
        val communeId = request.communeId
        if (communeId != null && communes.findById(communeId) == null) {
            throw ApiException(HttpStatus.BAD_REQUEST, mapOf("message" to "cette commune est inexistante !!!"))
        }
        val now = TimeFormat.now()
        val client = clients.save(
            ClientEntity(
                code = Codes.next(Codes.CLIENT, clients.count()),
                nom = request.nom!!.trim(),
                prenom = request.prenom!!.trim(),
                telephone = request.telephone!!.trim(),
                montantSouscription = souscription.montantSouscription,
                nombreTranches = souscription.nombreTranches,
                createdAt = now,
                updatedAt = now,
            ),
        )
        val address = addresses.save(
            AddressClientEntity(
                clientId = client.id,
                communeId = communeId,
                avenue = request.avenue!!.trim().lowercase(),
                quartier = request.quartier!!.trim(),
                numeroParcelle = request.numeroParcelle?.trim().orEmpty(),
                createdAt = now,
                updatedAt = now,
            ),
        ).toDomain()
        return ClientRegistration(address, souscription)
    }

    suspend fun authenticate(telephone: String, avenue: String): Client {
        val client = clients.findFirstByTelephone(telephone.trim())
            ?: throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "Ce numéro n'est pas identifié"))
        val linked = addressesOf(client.id)
        val first = linked.firstOrNull()
        if (first == null || first.avenue != avenue.trim().lowercase()) {
            throw ApiException(
                HttpStatus.FORBIDDEN,
                mapOf("message" to "L'avenue inserez n'est pas associé à votre numéro"),
            )
        }
        return client.toDomain(linked)
    }

    private suspend fun addressesOf(clientId: Long?): List<AddressClient> {
        if (clientId == null) return emptyList()
        return addresses.findAllByClientIdOrderByIdAsc(clientId).toList().map { it.toDomain() }
    }
}
