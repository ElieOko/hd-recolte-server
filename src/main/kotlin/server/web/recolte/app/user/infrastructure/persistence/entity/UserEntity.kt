package server.web.recolte.app.user.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("users")
data class UserEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("email")
    val email: String,
    @Column("email_verified_at")
    val emailVerifiedAt: LocalDateTime? = null,
    @Column("password")
    val password: String,
    @Column("remember_token")
    val rememberToken: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)

@Table("refresh_tokens")
data class RefreshTokenEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("token")
    val token: String,
    @Column("expires_at")
    val expiresAt: LocalDateTime,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
)
