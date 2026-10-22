package server.web.recolte.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import server.web.recolte.app.user.domain.model.User
import server.web.recolte.app.user.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.user.infrastructure.persistence.repository.UserRepository

@Service
class Auth(
    private val users: UserRepository,
) {
    suspend fun currentUser(): User? {
        val name = SecurityContextHolder.getContext().authentication?.name ?: return null
        val userId = name.toLongOrNull() ?: return null
        return users.findById(userId)?.toDomain()
    }
}
