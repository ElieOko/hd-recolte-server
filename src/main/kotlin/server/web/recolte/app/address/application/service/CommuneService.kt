package server.web.recolte.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import server.web.recolte.app.address.domain.model.Commune
import server.web.recolte.app.address.domain.model.request.CommuneRequest
import server.web.recolte.app.address.infrastructure.persistence.entity.CommuneEntity
import server.web.recolte.app.address.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.address.infrastructure.persistence.repository.CommuneRepository
import server.web.recolte.app.address.infrastructure.persistence.repository.DistrictRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.utils.TimeFormat

@Service
class CommuneService(
    private val repository: CommuneRepository,
    private val districts: DistrictRepository,
) {
    suspend fun findAll(): List<Commune> = repository.findAllByOrderByIdAsc().map { it.toDomain() }.toList()

    suspend fun findById(id: Long): Commune =
        repository.findById(id)?.toDomain()
            ?: throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "Commune introuvable"))

    suspend fun create(request: CommuneRequest): Commune {
        val districtId = request.districtId
            ?: throw ApiException(HttpStatus.BAD_REQUEST, mapOf("message" to "Le district est obligatoire"))
        if (districts.findById(districtId) == null) {
            throw ApiException(HttpStatus.BAD_REQUEST, mapOf("message" to "cet district est inexistant !!!"))
        }
        val now = TimeFormat.now()
        return repository.save(
            CommuneEntity(
                nom = request.nom.trim(),
                districtId = districtId,
                createdAt = now,
                updatedAt = now,
            ),
        ).toDomain()
    }
}
