import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.1"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "dev.surly"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(project(":core"))

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// mime-type detection
	implementation("org.apache.tika:tika-core:2.9.1")

	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")

	// security
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

	// password hashing
	implementation("at.favre.lib:bcrypt:0.10.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:r2dbc")

	// micrometer and otel
	implementation("io.micrometer:micrometer-registry-prometheus")
//	implementation("io.opentelemetry:opentelemetry-api:1.34.1")
//	implementation("io.opentelemetry:opentelemetry-sdk:1.34.1")
//	implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.34.1")
//	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:")
//	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:1.32.1-alpha")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
	springBoot.mainClass.set("dev.surly.images.ImagesApiApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}
