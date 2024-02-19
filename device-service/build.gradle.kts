import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

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
    maven { url = uri("https://maven-other.tuya.com/repository/maven-public/") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.postgresql:postgresql:42.6.0")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.liquibase:liquibase-core:4.24.0")

    // Tuya
    implementation("com.tuya:tuya-spring-boot-starter:1.3.2")

    // Google Guava for BiMap
    implementation("com.google.guava:guava:32.1.3-jre")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.withType<BootRun> {
    jvmArgs(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.management/sun.management=ALL-UNNAMED",
        "--add-opens=java.base/sun.net=ALL-UNNAMED",
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}
