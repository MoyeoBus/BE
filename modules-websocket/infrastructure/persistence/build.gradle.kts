plugins {
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.modulesScheduler.application)
    implementation(projects.modulesScheduler.common)
    implementation(projects.modulesScheduler.domain)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.kafka)

    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.mapstruct:mapstruct:1.6.2")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")

    kapt("org.mapstruct:mapstruct-processor:1.6.2")

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