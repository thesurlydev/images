package dev.surly.images.config

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
@EnableR2dbcRepositories
class DatabaseConfig(
    @Value("\${spring.r2dbc.host}") val host: String,
    @Value("\${spring.r2dbc.name}") val name: String,
    @Value("\${spring.r2dbc.username}") val username: String,
    @Value("\${spring.r2dbc.password}") val password: String,
    @Value("\${spring.r2dbc.port}") val port: Int
) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory = PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host(host)
            .database(name)
            .username(username)
            .password(password)
            .port(port).build()
    )

    @Bean
    fun databaseInitializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer =
        ConnectionFactoryInitializer().apply {
            setConnectionFactory(connectionFactory)
            setDatabasePopulator(CompositeDatabasePopulator().apply {
                addPopulators(
                    ResourceDatabasePopulator(
                        ClassPathResource("sql/schema.sql"),
                        ClassPathResource("sql/data.sql")
                    )
                )
            })
        }
}