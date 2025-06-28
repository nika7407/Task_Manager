plugins {
	java
	application
	id("jacoco")
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
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
	//password encryption
	implementation("org.springframework.security:spring-security-crypto:6.5.0")

	// Apache Commons
	implementation("org.apache.commons:commons-lang3:3.3.2")

	// Validation API
	implementation("jakarta.validation:jakarta.validation-api:3.1.1")
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

	//db for prod
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly ("org.postgresql:postgresql")


	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok:1.18.38")

	// MapStruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	// Optional: for nullable JSON support
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // faker
	testImplementation("net.datafaker:datafaker:2.4.2")
	implementation("net.datafaker:datafaker:2.4.3")
	//asserting
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
	//auth
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.2.RELEASE")
	testImplementation ("org.springframework.security:spring-security-test")
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

