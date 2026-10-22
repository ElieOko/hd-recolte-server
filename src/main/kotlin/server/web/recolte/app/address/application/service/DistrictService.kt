package server.web.recolte.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import server.web.recolte.app.address.domain.model.District
import server.web.recolte.app.address.domain.model.request.DistrictRequest
import server.web.recolte.app.address.infrastructure.persistence.entity.DistrictEntity
import server.web.recolte.app.address.infrastructure.persistence.mapper.toDomain
import server.web.recolte.app.address.infrastructure.persistence.repository.DistrictRepository
import server.web.recolte.exception.ApiException
import server.web.recolte.utils.TimeFormat

@Service
class DistrictService(
    private val repository: DistrictRepository,
) {
    suspend fun findAll(): List<District> = repository.findAllByOrderByIdAsc().map { it.toDomain() }.toList()

    suspend fun findById(id: Long): District =
        repository.findById(id)?.toDomain()
            ?: throw ApiException(HttpStatus.NOT_FOUND, mapOf("message" to "District introuvable"))

    suspend fun create(request: DistrictRequest): District {
        val now = TimeFormat.now()
        return repository.save(
            DistrictEntity(
                nom = request.nom.trim(),
                cityId = request.cityId,
                createdAt = now,
                updatedAt = now,
            ),
        ).toDomain()
    }
}
