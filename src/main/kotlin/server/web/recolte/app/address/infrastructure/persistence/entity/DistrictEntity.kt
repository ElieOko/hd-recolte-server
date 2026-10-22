package server.web.recolte.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("districts")
data class DistrictEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("nom")
    val nom: String? = null,
    @Column("city_id")
    val cityId: Long? = null,
    @Column("created_at")
    val createdAt: LocalDateTime? = null,
    @Column("updated_at")
    val updatedAt: LocalDateTime? = null,
)
