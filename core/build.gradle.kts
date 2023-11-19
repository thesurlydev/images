import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.5" apply false
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "dev.surly"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

// This is part of using spring's dependency management in isolation.
//      See https://docs.spring.io/spring-boot/docs/2.4.2/gradle-plugin/reference/htmlsingle/#managing-dependencies-dependency-management-plugin-using-in-isolation
the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	}
	api("org.springframework.boot:spring-boot-starter-data-r2dbc")
	api("org.springframework.boot:spring-boot-starter-json")
	api("org.postgresql:r2dbc-postgresql")

	api("org.slf4j:slf4j-api")
	api("ch.qos.logback:logback-classic")

	api("io.nats:jnats:2.17.1")

	api("com.fasterxml.jackson.module:jackson-module-kotlin")
	api("io.projectreactor.kotlin:reactor-kotlin-extensions")
	api("org.jetbrains.kotlin:kotlin-reflect")
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	api("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
	api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:r2dbc")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}