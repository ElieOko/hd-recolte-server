package server.web.recolte.app.trash.domain.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ClientTrashRequest(
    val clientId: Long? = null,
    val stateTrashId: Long? = null,
)

data class StateTrashRequest(
    @field:NotBlank
    val nom: String = "",
    @field:NotNull
    val niveau: Int? = null,
)
