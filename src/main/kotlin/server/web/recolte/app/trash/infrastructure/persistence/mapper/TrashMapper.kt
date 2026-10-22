package server.web.recolte.app.trash.infrastructure.persistence.mapper

import server.web.recolte.app.client.domain.model.Client
import server.web.recolte.app.trash.domain.model.ClientTrash
import server.web.recolte.app.trash.domain.model.StateTrash
import server.web.recolte.app.trash.infrastructure.persistence.entity.ClientTrashEntity
import server.web.recolte.app.trash.infrastructure.persistence.entity.StateTrashEntity
import server.web.recolte.utils.TimeFormat

fun StateTrashEntity.toDomain() = StateTrash(
    id = id,
    niveau = niveau,
    nom = nom,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)

fun ClientTrashEntity.toDomain(client: Client? = null) = ClientTrash(
    id = id,
    clientId = clientId,
    stateTrashId = stateTrashId,
    isActive = if (isActive) 1 else 0,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
    client = client,
)
