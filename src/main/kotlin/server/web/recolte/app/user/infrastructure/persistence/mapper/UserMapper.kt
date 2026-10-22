package server.web.recolte.app.user.infrastructure.persistence.mapper

import server.web.recolte.app.user.domain.model.User
import server.web.recolte.app.user.infrastructure.persistence.entity.UserEntity
import server.web.recolte.utils.TimeFormat

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    email = email,
    createdAt = TimeFormat.format(createdAt),
)
