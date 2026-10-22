package server.web.recolte.app.agent.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import server.web.recolte.app.agent.domain.model.Agent
import server.web.recolte.app.agent.domain.model.request.AgentStoreRequest
import server.web.recolte.app.agent.infrastructure.persistence.entity.AgentEntity
import server.web.recolte.app.agent.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.agent.infrastructure.persistence.repository.AgentRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.utils.Codes
import server.web.recolte.utils.TimeFormat

@Service
class AgentService(
    private val repository: AgentRepository,
) {
    suspend fun findAll(): List<Agent> = repository.findAllByOrderByIdAsc().map { it.toDomain() }.toList()

    @Transactional
    suspend fun create(request: AgentStoreRequest): Agent {
        val nom = request.nom!!.trim()
        val prenom = request.prenom!!.trim()
        val postnom = request.postnom?.trim().orEmpty()
        if (repository.countByNomAndPostnomAndPrenom(nom, postnom, prenom) > 0) {
            throw ApiException(HttpStatus.FORBIDDEN, mapOf("message" to "Ce compte est déjà enregistrer"))
        }
        val now = TimeFormat.now()
        return repository.save(
            AgentEntity(
                code = Codes.next(Codes.AGENT, repository.count()),
                nom = nom,
                postnom = postnom,
                prenom = prenom,
                telephone = request.telephone?.trim().orEmpty(),
                genre = request.genre?.trim(),
                address = request.address?.trim().orEmpty(),
                dateNaissance = request.dateNaissance?.trim().orEmpty(),
                createdAt = now,
                updatedAt = now,
            ),
        ).toDomain()
    }

    suspend fun authenticate(code: String): Agent {
        val agent = repository.findByCode(code.trim().lowercase())
            ?: throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "Utilisateur non valide"))
        return agent.toDomain()
    }
}
