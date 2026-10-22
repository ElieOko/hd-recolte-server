package server.web.recolte.app.agent.domain.model.request

import com.fasterxml.jackson.annotation.JsonAlias

data class AgentStoreRequest(
    val nom: String? = null,
    val postnom: String? = null,
    val prenom: String? = null,
    val genre: String? = null,
    val telephone: String? = null,
    @JsonAlias("adresse")
    val address: String? = null,
    val dateNaissance: String? = null,
)

data class AgentAuthRequest(
    val code: String? = null,
)
