package server.web.recolte.app.address.domain.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class DistrictRequest(
    @field:NotBlank
    val nom: String = "",
    val cityId: Long? = null,
)

data class CommuneRequest(
    @field:NotBlank
    val nom: String = "",
    @field:NotNull
    val districtId: Long? = null,
)
