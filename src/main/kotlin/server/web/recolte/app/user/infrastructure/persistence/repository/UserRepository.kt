package server.web.recolte.app.user.infrastructure.persistence.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import server.web.recolte.app.user.infrastructure.persistence.entity.RefreshTokenEntity
import server.web.recolte.app.user.infrastructure.persistence.entity.UserEntity

interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {
    suspend fun findByEmail(email: String): UserEntity?

    fun findAllByOrderByIdAsc(): Flow<UserEntity>
}

interface RefreshTokenRepository : CoroutineCrudRepository<RefreshTokenEntity, Long> {
    suspend fun findByToken(token: String): RefreshTokenEntity?

    suspend fun deleteByUserId(userId: Long)
}
