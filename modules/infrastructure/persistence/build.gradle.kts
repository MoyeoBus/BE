plugins {
    kotlin("jvm")
    kotlin("plugin.jpa") version "1.9.25"
}
tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

dependencies {
    implementation(projects.modules.domain)
    implementation(projects.modules.common)
    implementation(projects.modules.application)
    implementation(libs.spring.boot.starter.jpa)

    implementation("com.mysql:mysql-connector-j:8.3.0")

    implementation("org.flywaydb:flyway-core:10.20.1")
    implementation("org.flywaydb:flyway-mysql:10.20.1")

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
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}