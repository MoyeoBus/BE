plugins {
    kotlin("jvm")
}
tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

dependencies {
    implementation(projects.modulesScheduler.common)
    // Only need Spring annotations (@Service) for this module
    implementation("org.springframework:spring-context")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}