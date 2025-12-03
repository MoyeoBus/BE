plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.modulesScheduler.application)
    implementation(projects.modulesScheduler.domain)
    implementation(libs.spring.boot.starter.web)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}