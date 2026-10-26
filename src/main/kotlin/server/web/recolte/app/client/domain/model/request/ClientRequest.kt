package server.web.recolte.app.client.domain.model.request

import com.fasterxml.jackson.annotation.JsonAlias

data class ClientStoreRequest(
    val nom: String? = null,
    val prenom: String? = null,
    val telephone: String? = null,
    val communeId: Long? = null,
    val avenue: String? = null,
    val quartier: String? = null,
    val numeroParcelle: String? = null,
    @JsonAlias("montant")
    val montantSouscription: Long? = null,
    val nombreTranches: Int? = null,
    val tranche: String? = null,
)

data class ClientAuthRequest(
    val telephone: String? = null,
    val avenue: String? = null,
)
