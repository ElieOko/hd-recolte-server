package server.web.recolte.app.agent.domain.model

data class Agent(
    val id: Long? = null,
    val code: String? = null,
    val cityId: Int? = null,
    val nom: String = "",
    val postnom: String? = null,
    val prenom: String = "",
    val telephone: String? = null,
    val genre: String? = null,
    val address: String? = null,
    val dateNaissance: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
