package server.web.recolte.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import server.web.recolte.exception.CustomAccessDeniedHandler
import server.web.recolte.exception.CustomAuthEntryPoint
import server.web.recolte.security.JwtAuthFilter

@Configuration
@EnableWebSecurity
class ServerConfig(
    private val customAuthEntryPoint: CustomAuthEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
    private val jwtAuthFilter: JwtAuthFilter,
) : WebMvcConfigurer {
    private val log = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        log.info("ServerConfig est actif")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
        throw UsernameNotFoundException(username)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .securityContext { it.securityContextRepository(RequestAttributeSecurityContextRepository()) }
            .exceptionHandling {
                it.authenticationEntryPoint(customAuthEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
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
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .build()
}
