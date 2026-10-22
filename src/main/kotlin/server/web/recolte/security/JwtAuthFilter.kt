package server.web.recolte.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {
    override fun shouldNotFilterAsyncDispatch(): Boolean = false

    private val matcher = AntPathMatcher()
    private val publicPaths = listOf(
        "/",
        "/actuator/health",
        "/actuator/health/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/api/client",
        "/api/client/**",
        "/api/agent",
        "/api/agent/**",
        "/api/trash",
        "/api/trash/**",
        "/api/*/public/**",
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val path = request.requestURI
        if (publicPaths.any { matcher.match(it, path) }) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ") || !jwtService.validateAccessToken(authHeader)) {
            writeError(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing JWT token")
            return
        }

        val userId = jwtService.getUserIdFromToken(authHeader)
        val authentication = UsernamePasswordAuthenticationToken(userId, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }

    private fun writeError(
        response: HttpServletResponse,
        request: HttpServletRequest,
        status: Int,
        message: String,
    ) {
        response.status = status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        val body = mapOf(
            "status" to status,
            "error" to "Unauthorized",
            "message" to message,
            "path" to request.requestURI,
        )
        ObjectMapper().writeValue(response.writer, body)
    }
}
