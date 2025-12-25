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
    implementation(projects.modulesWebsocket.common)
    implementation(projects.modulesWebsocket.domain)
    // Only need Spring annotations (@Service) for this module
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}