package server.web.recolte.app.user.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import server.web.recolte.app.user.application.service.AuthService
import server.web.recolte.app.user.application.service.UserService
import server.web.recolte.app.user.domain.model.request.LoginRequest
import server.web.recolte.app.user.domain.model.request.RefreshRequest
import server.web.recolte.app.user.domain.model.request.RegisterRequest
import server.web.recolte.route.GlobalRoute
import server.web.recolte.route.user.UserScope
import server.web.recolte.security.Auth

@Tag(name = "Auth", description = "Comptes administrateurs et JWT")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping(
        "/{version}/${UserScope.PUBLIC_AUTH}/register",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Map<String, Any>> {
        val token = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            mapOf(
                "message" to "Compte créer avec succès",
                "access_token" to token.accessToken,
                "refresh_token" to token.refreshToken,
                "user" to token.user,
            ),
        )
    }

    @PostMapping(
        "/{version}/${UserScope.PUBLIC_AUTH}/login",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Map<String, Any>> {
        val token = authService.login(request)
        return ResponseEntity.ok(
            mapOf(
                "message" to "login success",
                "access_token" to token.accessToken,
                "refresh_token" to token.refreshToken,
                "user" to token.user,
            ),
        )
    }

    @PostMapping(
        "/{version}/${UserScope.PUBLIC_AUTH}/refresh",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun refresh(@Valid @RequestBody request: RefreshRequest): ResponseEntity<Map<String, Any>> {
        val token = authService.refresh(request.refreshToken)
        return ResponseEntity.ok(
            mapOf(
                "message" to "login success",
                "access_token" to token.accessToken,
                "refresh_token" to token.refreshToken,
                "user" to token.user,
            ),
        )
    }
}

@Tag(name = "User", description = "Utilisateurs administrateurs")
@RestController
@RequestMapping(GlobalRoute.ROOT)
class UserController(
    private val users: UserService,
    private val auth: Auth,
) {
    @GetMapping("/{version}/${UserScope.PROTECTED}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun index(): ResponseEntity<Map<String, Any?>> {
        val current = auth.currentUser()
        return ResponseEntity.ok(
            mapOf(
                "user" to current,
                "users" to users.findAll(),
            ),
        )
    }
}
