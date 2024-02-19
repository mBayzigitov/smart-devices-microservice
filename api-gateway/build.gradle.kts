import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.17"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.openapi.generator") version "6.6.0"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "ru.handh"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}