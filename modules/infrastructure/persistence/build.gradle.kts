plugins {
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.modules.domain)
    implementation(projects.modules.common)
    implementation(projects.modules.application)
    implementation(libs.spring.boot.starter.jpa)
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.mapstruct:mapstruct:1.6.2")
    kapt("org.mapstruct:mapstruct-processor:1.6.2")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    implementation("com.graphhopper:graphhopper-core:8.0") {
        exclude(group = "com.fasterxml.jackson.core")
        exclude(group = "com.fasterxml.jackson.dataformat")
        exclude(group = "com.fasterxml.jackson.datatype")
        exclude(group = "com.fasterxml.jackson.module")
    }
    implementation("com.google.protobuf:protobuf-java:3.21.9")

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
}

tasks {
    jar { enabled = true }
    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }
}

kotlin {
    jvmToolchain(21)
}