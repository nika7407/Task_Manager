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
	runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.data:spring-data-jpa:3.5.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("jakarta.validation:jakarta.validation-api:3.1.1")
	implementation("org.projectlombok:lombok:1.18.38")

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

