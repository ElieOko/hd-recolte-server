package server.web.recolte.app.client.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("clients")
data class ClientEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("code")
    val code: String? = null,
    @Column("nom")
    val nom: String,
    @Column("prenom")
    val prenom: String,
    @Column("telephone")
    val telephone: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)

@Table("address_clients")
data class AddressClientEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("client_id")
    val clientId: Long? = null,
    @Column("commune_id")
    val communeId: Long? = null,
    @Column("avenue")
    val avenue: String,
    @Column("numero_parcelle")
    val numeroParcelle: String? = null,
    @Column("quartier")
    val quartier: String,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)
