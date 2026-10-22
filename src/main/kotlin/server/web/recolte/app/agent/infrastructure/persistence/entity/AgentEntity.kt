package server.web.recolte.app.agent.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("agents")
data class AgentEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("code")
    val code: String? = null,
    @Column("city_id")
    val cityId: Int = 1,
    @Column("nom")
    val nom: String,
    @Column("postnom")
    val postnom: String,
    @Column("prenom")
    val prenom: String,
    @Column("telephone")
    val telephone: String? = null,
    @Column("genre")
    val genre: String? = null,
    @Column("address")
    val address: String? = null,
    @Column("date_naissance")
    val dateNaissance: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)
