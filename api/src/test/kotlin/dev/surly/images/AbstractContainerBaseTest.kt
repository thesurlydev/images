package dev.surly.images

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.junit.jupiter.api.BeforeAll

@Testcontainers
open class AbstractContainerBaseTest {

  companion object {
    @Container
    val container = PostgreSQLContainer("postgres:latest").apply {
      withUsername("shane")
      withPassword("shane")
      withDatabaseName("images")
    }

    @JvmStatic
    @DynamicPropertySource
    fun properties(registry: DynamicPropertyRegistry) {
      registry.add("spring.r2dbc.url") { "r2dbc:postgresql://${container.host}:${container.firstMappedPort}/images" }
      registry.add("spring.r2dbc.host") { container.host }
      registry.add("spring.r2dbc.port") { container.firstMappedPort.toString() }
      registry.add("spring.r2dbc.username", container::getUsername)
      registry.add("spring.r2dbc.password", container::getPassword)
      registry.add("spring.r2dbc.name", container::getDatabaseName)
    }

    @BeforeAll
    @JvmStatic
    fun setup() {
      container.start()
    }
  }
}

