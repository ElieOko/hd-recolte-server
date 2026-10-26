package server.web.recolte.app.client.domain.model

data class Client(
    val id: Long? = null,
    val code: String? = null,
    val nom: String = "",
    val prenom: String = "",
    val telephone: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val addressClient: List<AddressClient> = emptyList(),
)

data class AddressClient(
    val id: Long? = null,
    val clientId: Long? = null,
    val communeId: Long? = null,
    val avenue: String = "",
    val numeroParcelle: String = "",
    val quartier: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
