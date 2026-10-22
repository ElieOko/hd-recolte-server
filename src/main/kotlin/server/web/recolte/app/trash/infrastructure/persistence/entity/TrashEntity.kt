package server.web.recolte.app.trash.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("state_trashes")
data class StateTrashEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("niveau")
    val niveau: Int,
    @Column("nom")
    val nom: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)

@Table("client_trashes")
data class ClientTrashEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("client_id")
    val clientId: Long? = null,
    @Column("state_trash_id")
    val stateTrashId: Long? = null,
    @Column("is_active")
    val isActive: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)
