package server.web.recolte.utils

import org.springframework.http.HttpStatus
import server.web.recolte.exception.ApiException

object FieldRules {
    fun required(fields: Map<String, String?>, status: HttpStatus = HttpStatus.BAD_REQUEST) {
        val missing = fields.entries.firstOrNull { it.value.isNullOrBlank() } ?: return
        throw ApiException(
            status,
            mapOf("message" to mapOf(missing.key to listOf("The ${missing.key} field is required."))),
        )
    }

    fun requiredIds(fields: Map<String, Long?>, status: HttpStatus) {
        val missing = fields.entries.firstOrNull { it.value == null } ?: return
        throw ApiException(
            status,
            mapOf("message" to mapOf(missing.key to listOf("The ${missing.key} field is required."))),
        )
    }
}
