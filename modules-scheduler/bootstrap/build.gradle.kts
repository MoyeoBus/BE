plugins {
    kotlin("jvm")
}
tasks.jar {
    enabled = false
}

tasks.bootJar {
    enabled = true
    mainClass.set("com.moyeobus.scheduler.MoyeobusSchedulerApplicationKt")
}


dependencies {
    implementation(projects.modulesScheduler.common)
    implementation(projects.modulesScheduler.external)
    implementation(projects.modulesScheduler.domain)
    implementation(projects.modulesScheduler.application)
    implementation(projects.modulesScheduler.infrastructure.persistence)
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
