package server.web.recolte.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder(
    private val encoder: PasswordEncoder,
) {
    fun encode(raw: String): String = requireNotNull(encoder.encode(raw))

    fun matches(raw: String, hashed: String): Boolean = encoder.matches(raw, hashed)
}
