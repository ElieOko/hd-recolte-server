package server.web.recolte.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApi(exception: ApiException): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.status(exception.status).body(exception.body)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleInvalid(exception: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val field = exception.bindingResult.fieldErrors.firstOrNull()
        val message: Any = if (field != null) {
            mapOf(field.field to listOf(field.defaultMessage ?: "invalide"))
        } else {
            "Requête invalide"
        }
        return ResponseEntity.badRequest().body(mapOf("message" to message))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadable(exception: HttpMessageNotReadableException): ResponseEntity<ErrorResponseMessageDto> {
        logger.warn("Requête illisible", exception)
        return ResponseEntity.badRequest().body(ErrorResponseMessageDto("Corps de requête invalide"))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(exception: Exception): ResponseEntity<ErrorResponseDto> {
        logger.error("Erreur interne", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponseDto(
                message = "Internal server error",
                detailMessage = exception.message ?: "unexpected",
                errorTime = LocalDateTime.now(),
            ),
        )
    }
}
