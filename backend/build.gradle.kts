plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.bookease"
version = "1.0.0"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

repositories { mavenCentral() }

dependencies {
    
dependencies {
    // Spring Boot REST API
    implementation("org.springframework.boot:spring-boot-starter-web")

    // MongoDB database integration
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Kotlin support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

}

tasks.withType<Test> { useJUnitPlatform() }