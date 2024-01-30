package dev.surly.images

import dev.surly.images.config.DatabaseConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseConfig::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
class ImagesApplicationTests: AbstractContainerBaseTest() {

	@Test
	fun contextLoads() {
	}

}
