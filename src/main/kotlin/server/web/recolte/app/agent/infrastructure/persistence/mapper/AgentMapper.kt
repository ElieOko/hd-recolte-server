package server.web.recolte.app.agent.infrastructure.persistence.mapper

import server.web.recolte.app.agent.domain.model.Agent
import server.web.recolte.app.agent.infrastructure.persistence.entity.AgentEntity
import server.web.recolte.utils.TimeFormat

fun AgentEntity.toDomain() = Agent(
    id = id,
    code = code,
    cityId = cityId,
    nom = nom,
    postnom = postnom,
    prenom = prenom,
    telephone = telephone,
    genre = genre,
    address = address,
    dateNaissance = dateNaissance,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)
