package server.web.recolte.app.client.infrastructure.persistence.mapper

import server.web.recolte.app.client.domain.model.AddressClient
import server.web.recolte.app.client.domain.model.Client
import server.web.recolte.app.client.infrastructure.persistence.entity.AddressClientEntity
import server.web.recolte.app.client.infrastructure.persistence.entity.ClientEntity
import server.web.recolte.utils.TimeFormat

fun AddressClientEntity.toDomain() = AddressClient(
    id = id,
    clientId = clientId,
    communeId = communeId ?: 0,
    avenue = avenue,
    numeroParcelle = numeroParcelle.orEmpty(),
    quartier = quartier,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)

fun ClientEntity.toDomain(addresses: List<AddressClient> = emptyList()) = Client(
    id = id,
    code = code,
    nom = nom,
    prenom = prenom,
    telephone = telephone,
    addressClient = addresses,
    createdAt = TimeFormat.format(createdAt),
    updatedAt = TimeFormat.format(updatedAt),
)
