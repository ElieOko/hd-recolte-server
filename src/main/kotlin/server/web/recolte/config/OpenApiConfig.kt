package server.web.recolte.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun recolteOpenApi(): OpenAPI =
        OpenAPI().info(
            Info()
                .title("HD Récolte")
                .description("API de collecte des poubelles : clients, agents et vidage.")
                .version("1.0.0"),
        )
}
