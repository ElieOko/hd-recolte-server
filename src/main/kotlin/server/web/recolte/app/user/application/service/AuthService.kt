package server.web.recolte.app.user.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import server.web.recolte.app.user.domain.model.AuthToken
import server.web.recolte.app.user.domain.model.User
import server.web.recolte.app.user.domain.model.request.LoginRequest
import server.web.recolte.app.user.domain.model.request.RegisterRequest
import server.web.recolte.app.user.infrastructure.persistence.entity.RefreshTokenEntity
import server.web.recolte.app.user.infrastructure.persistence.entity.UserEntity
import server.web.recolte.app.user.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.user.infrastructure.persistence.repository.RefreshTokenRepository
import server.web.recolte.app.user.infrastructure.persistence.repository.UserRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.security.HashEncoder
import server.web.recolte.security.JwtService
import server.web.recolte.utils.TimeFormat

@Service
class UserService(
    private val users: UserRepository,
) {
    suspend fun findAll(): List<User> = users.findAllByOrderByIdAsc().map { it.toDomain() }.toList()
}

@Service
class AuthService(
    private val users: UserRepository,
    private val refreshTokens: RefreshTokenRepository,
    private val hashEncoder: HashEncoder,
    private val jwtService: JwtService,
) {
    @Transactional
    suspend fun register(request: RegisterRequest): AuthToken {
        val email = request.email.trim().lowercase()
        if (users.findByEmail(email) != null) {
            throw ApiException(HttpStatus.CONFLICT, mapOf("message" to "Cet email est déjà utilisé"))
        }
        val now = TimeFormat.now()
        val saved = users.save(
            UserEntity(
                name = request.name.trim(),
                email = email,
                password = hashEncoder.encode(request.password),
                createdAt = now,
                updatedAt = now,
            ),
        )
        return issue(saved)
    }

    @Transactional
    suspend fun login(request: LoginRequest): AuthToken {
        val user = users.findByEmail(request.email.trim().lowercase())
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Utilisateur non valide"))
        if (!hashEncoder.matches(request.password, user.password)) {
            throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Mot de passe incorrecte"))
        }
        return issue(user)
    }

    @Transactional
    suspend fun refresh(refreshToken: String): AuthToken {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Refresh token invalide"))
        }
        val stored = refreshTokens.findByToken(refreshToken)
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Refresh token invalide"))
        if (stored.expiresAt.isBefore(TimeFormat.now())) {
            throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Refresh token expiré"))
        }
        val user = users.findById(stored.userId)
            ?: throw ApiException(HttpStatus.UNAUTHORIZED, mapOf("message" to "Utilisateur non valide"))
        return issue(user)
    }

    private suspend fun issue(user: UserEntity): AuthToken {
        val userId = user.id!!.toString()
        val access = jwtService.generateAccessToken(userId)
        val refresh = jwtService.generateRefreshToken(userId)
        refreshTokens.deleteByUserId(user.id)
        refreshTokens.save(
            RefreshTokenEntity(
                userId = user.id,
                token = refresh,
                expiresAt = TimeFormat.now().plusSeconds(jwtService.refreshTokenValidityMs / 1000),
                createdAt = TimeFormat.now(),
            ),
        )
        return AuthToken(accessToken = access, refreshToken = refresh, user = user.toDomain())
    }
}
