package server.web.recolte.app.address.infrastructure.persistence.mapper

import server.web.recolte.app.address.domain.model.Commune
import server.web.recolte.app.address.domain.model.District
import server.web.recolte.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.recolte.app.address.infrastructure.persistence.entity.DistrictEntity
import server.web.recolte.utils.TimeFormat

fun DistrictEntity.toDomain() = District(
    id = id,
    nom = nom,
    cityId = cityId,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)

fun CommuneEntity.toDomain() = Commune(
    id = id,
    nom = nom,
    districtId = districtId,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)
