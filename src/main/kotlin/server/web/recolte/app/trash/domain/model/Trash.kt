package server.web.recolte.app.trash.domain.model

import com.fasterxml.jackson.annotation.JsonProperty
import server.web.recolte.app.client.domain.model.Client

data class StateTrash(
    val id: Long? = null,
    val niveau: Int? = null,
    val nom: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

data class ClientTrash(
    val id: Long? = null,
    val clientId: Long? = null,
    val stateTrashId: Long? = null,
    @JsonProperty("is_active")
    val isActive: Int = 1,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val client: Client? = null,
)
