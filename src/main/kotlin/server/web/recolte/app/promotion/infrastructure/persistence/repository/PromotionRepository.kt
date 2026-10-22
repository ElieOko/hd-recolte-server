package server.web.recolte.app.promotion.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.promotion.infrastructure.persistence.entity.PromotionEntity

interface PromotionRepository : CoroutineCrudRepository<PromotionEntity, Long> {
    fun findAllByOrderByIdAsc(): Flow<PromotionEntity>
}
