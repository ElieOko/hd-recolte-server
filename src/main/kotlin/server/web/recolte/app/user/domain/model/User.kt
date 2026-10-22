package server.web.recolte.app.user.domain.model

data class User(
    val id: Long? = null,
    val name: String = "",
    val email: String = "",
    val createdAt: String? = null,
)

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
)
