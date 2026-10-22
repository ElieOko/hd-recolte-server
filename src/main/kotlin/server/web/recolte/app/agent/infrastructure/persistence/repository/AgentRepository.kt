package server.web.recolte.app.agent.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.agent.infrastructure.persistence.entity.AgentEntity

interface AgentRepository : CoroutineCrudRepository<AgentEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<AgentEntity>

    suspend fun findByCode(code: String): AgentEntity?

    suspend fun countByNomAndPostnomAndPrenom(nom: String, postnom: String, prenom: String): Long
}
