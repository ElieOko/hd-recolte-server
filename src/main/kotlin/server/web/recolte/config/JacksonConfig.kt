package server.web.recolte.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.JacksonModule
import tools.jackson.module.kotlin.KotlinModule

@Configuration
class JacksonConfig {
    @Bean
    fun kotlinModule(): JacksonModule = KotlinModule.Builder().build()
}
