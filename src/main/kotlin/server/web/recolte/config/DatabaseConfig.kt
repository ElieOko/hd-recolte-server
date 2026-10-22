package server.web.recolte.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import server.web.recolte.utils.Mode

@Configuration
@Profile(Mode.DEV, Mode.PROD)
class DatabaseConfig {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        log.info("Schéma PostgreSQL géré par Flyway")
    }
}
