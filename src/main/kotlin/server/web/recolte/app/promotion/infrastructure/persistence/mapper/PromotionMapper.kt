package server.web.recolte.app.promotion.infrastructure.persistence.mapper

import server.web.recolte.app.promotion.domain.model.Promotion
import server.web.recolte.app.promotion.infrastructure.persistence.entity.PromotionEntity
import server.web.recolte.utils.TimeFormat

fun PromotionEntity.toDomain() = Promotion(
    id = id,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)
