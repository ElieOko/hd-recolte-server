package server.web.recolte.app.client.domain.model.request

data class ClientStoreRequest(
    val nom: String? = null,
    val prenom: String? = null,
    val telephone: String? = null,
    val communeId: Long? = null,
    val avenue: String? = null,
    val quartier: String? = null,
    val numeroParcelle: String? = null,
)

data class ClientAuthRequest(
    val telephone: String? = null,
    val avenue: String? = null,
)
