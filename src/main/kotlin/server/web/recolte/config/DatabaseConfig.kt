package server.web.recolte.config

import jakarta.annotation.PostConstruct
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.flyway.autoconfigure.FlywayProperties
import org.springframework.boot.r2dbc.autoconfigure.R2dbcProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import server.web.recolte.utils.Mode

@Configuration
@EnableConfigurationProperties(
    FlywayProperties::class,
    R2dbcProperties::class
)
class DatabaseConfig {

     @Bean(initMethod = "migrate")
      fun flyway(
          flywayProperties: FlywayProperties,
          r2dbcProperties: R2dbcProperties
      ): Flyway {

          return Flyway.configure()
              .dataSource(
                  "jdbc:postgresql://aws-1-eu-west-3.pooler.supabase.com:5432/postgres",          // JDBC URL
                  "postgres.yssvrnnqlsvcyuhettzp",
                  ""
              )
              .locations(*flywayProperties.locations.toTypedArray())
              .baselineOnMigrate(true)
              .load()
      }
}
