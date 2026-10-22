package server.web.recolte.app.user.domain.model.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:NotBlank
    val name: String = "",
    @field:NotBlank
    @field:Email
    val email: String = "",
    @field:NotBlank
    val password: String = "",
)

data class LoginRequest(
    @field:NotBlank
    @field:Email
    val email: String = "",
    @field:NotBlank
    val password: String = "",
)

data class RefreshRequest(
    @field:NotBlank
    @JsonAlias("refreshToken")
    val refreshToken: String = "",
)
