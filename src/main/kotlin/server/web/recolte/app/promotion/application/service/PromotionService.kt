package server.web.recolte.app.promotion.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import server.web.recolte.app.promotion.domain.model.Promotion
import server.web.recolte.app.promotion.infrastructure.persistence.entity.PromotionEntity
import server.web.recolte.app.promotion.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.promotion.infrastructure.persistence.repository.PromotionRepository
import server.web.recolte.utils.TimeFormat

@Service
class PromotionService(
    private val repository: PromotionRepository,
) {
    suspend fun findAll(): List<Promotion> = repository.findAllByOrderByIdAsc().map { it.toDomain() }.toList()

    suspend fun create(): Promotion {
        val now = TimeFormat.now()
        return repository.save(PromotionEntity(createdAt = now, updatedAt = now)).toDomain()
    }
}
