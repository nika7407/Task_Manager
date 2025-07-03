plugins {
	application
	id("jacoco")
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.sentry.jvm.gradle") version "5.8.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

application {
	mainClass.set("hexlet.code.AppApplication")
}

dependencies {
	// Runtime
	runtimeOnly("com.h2database:h2")

	// Spring Boot
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Password encryption
	implementation("org.springframework.security:spring-security-crypto:6.5.0")

	// Apache Commons
	implementation("org.apache.commons:commons-lang3:3.3.2")

	// Validation API
	implementation("jakarta.validation:jakarta.validation-api:3.1.1")
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

	// PostgreSQL for production
	implementation("org.postgresql:postgresql:42.7.7")
	runtimeOnly("org.postgresql:postgresql")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok:1.18.38")

	// MapStruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	// Optional: Nullable JSON support
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("net.datafaker:datafaker:2.4.2")
	implementation("net.datafaker:datafaker:2.4.3")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")

	// Auth
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.2.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-jwt:1.1.1.RELEASE")
	runtimeOnly("org.springframework.security:spring-security-jwt:1.1.1.RELEASE")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.withType<JavaCompile> {
	options.annotationProcessorPath = configurations.annotationProcessor.get()
}

buildscript {
	repositories {
		mavenCentral()
	}
}

// Conditional Sentry setup
val sentryToken = System.getenv("SENTRY_AUTH_TOKEN")

if (!sentryToken.isNullOrBlank()) {
	sentry {
		includeSourceContext = true
		org = "niks-s4"
		projectName = "java-spring-boot"
		authToken = sentryToken
	}
} else {
	logger.lifecycle("Sentry is disabled: no SENTRY_AUTH_TOKEN found in environment.")
	tasks.matching { it.name.startsWith("sentry") }.configureEach {
		enabled = false
	}
}
