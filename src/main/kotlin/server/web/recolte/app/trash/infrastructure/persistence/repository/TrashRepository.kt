package server.web.recolte.app.trash.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.trash.infrastructure.persistence.entity.ClientTrashEntity
import server.web.recolte.app.trash.infrastructure.persistence.entity.StateTrashEntity

interface StateTrashRepository : CoroutineCrudRepository<StateTrashEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<StateTrashEntity>
}

interface ClientTrashRepository : CoroutineCrudRepository<ClientTrashEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<ClientTrashEntity>

    suspend fun findFirstByClientIdOrderByIdAsc(clientId: Long): ClientTrashEntity?

    suspend fun findFirstByClientIdAndStateTrashIdAndIsActive(
        clientId: Long,
        stateTrashId: Long,
        isActive: Boolean,
    ): ClientTrashEntity?
}
