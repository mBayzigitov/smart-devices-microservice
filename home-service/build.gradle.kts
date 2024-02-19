import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.16"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.postgresql:postgresql:42.6.0")

    implementation("io.github.microutils:kotlin-logging:3.0.5")

    implementation("org.liquibase:liquibase-core:4.24.0")

    implementation("net.javacrumbs.shedlock:shedlock-spring:5.9.1")
    implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.9.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.testcontainers:postgresql:1.17.6")

}

allOpen {
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embeddable")
}

// solution for nullPointerException in verify
tasks.test {
    jvmArgs = listOf("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED")
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
