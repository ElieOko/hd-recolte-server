package server.web.recolte.app.client.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.client.infrastructure.persistence.entity.AddressClientEntity
import server.web.recolte.app.client.infrastructure.persistence.entity.ClientEntity

interface ClientRepository : CoroutineCrudRepository<ClientEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<ClientEntity>

    suspend fun findFirstByTelephone(telephone: String): ClientEntity?
}

interface AddressClientRepository : CoroutineCrudRepository<AddressClientEntity, Long> {
    fun findAllByClientIdOrderByIdAsc(clientId: Long): Flow<AddressClientEntity>
}
