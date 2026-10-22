package server.web.recolte.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") jwtSecret: String,
    @Value("\${jwt.access-token-expiration}") private val accessTokenValidityMs: Long,
    @Value("\${jwt.refresh-token-expiration}") val refreshTokenValidityMs: Long,
) {
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    fun generateAccessToken(userId: String): String = generateToken(userId, "access", accessTokenValidityMs)

    fun generateRefreshToken(userId: String): String = generateToken(userId, "refresh", refreshTokenValidityMs)

    fun validateAccessToken(token: String): Boolean = tokenType(token) == "access"

    fun validateRefreshToken(token: String): Boolean = tokenType(token) == "refresh"

    fun getUserIdFromToken(token: String): String =
        parseAllClaims(token)?.subject ?: throw IllegalArgumentException("Invalid token.")

    private fun generateToken(userId: String, type: String, expiry: Long): String {
        val now = Date()
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(Date(now.time + expiry))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun tokenType(token: String): String? = parseAllClaims(token)?.get("type") as? String

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (_: Exception) {
            null
        }
    }
}
