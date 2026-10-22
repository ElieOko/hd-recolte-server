package server.web.recolte.app.address.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.address.infrastructure.persistence.entity.CommuneEntity

interface CommuneRepository : CoroutineCrudRepository<CommuneEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<CommuneEntity>
}
