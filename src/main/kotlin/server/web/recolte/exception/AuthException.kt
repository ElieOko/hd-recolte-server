package server.web.recolte.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        write(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", "Accès refusé : token invalide ou manquant.")
    }
}

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        write(response, request, HttpServletResponse.SC_FORBIDDEN, "Forbidden", "Accès interdit : vous n'avez pas les autorisations nécessaires.")
    }
}

private fun write(
    response: HttpServletResponse,
    request: HttpServletRequest,
    status: Int,
    error: String,
    message: String,
) {
    response.contentType = "application/json"
    response.characterEncoding = "UTF-8"
    response.status = status
    val body = mapOf(
        "status" to status,
        "error" to error,
        "message" to message,
        "path" to request.requestURI,
    )
    ObjectMapper().writeValue(response.outputStream, body)
}
