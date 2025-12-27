plugins {
    kotlin("jvm")
}
tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
}


dependencies {
    implementation(projects.modulesWebsocket.common)
    implementation(projects.modulesWebsocket.application)
    implementation(projects.modulesWebsocket.infrastructure.persistence)
    implementation(libs.spring.boot.starter.jpa)
    implementation(libs.bundles.bootstrap)
    testImplementation(libs.bundles.test)
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.mockk)
    testImplementation(libs.database.mariadb)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}
