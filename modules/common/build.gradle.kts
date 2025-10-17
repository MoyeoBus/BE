tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

dependencies {
    implementation(libs.bundles.jackson)
    implementation(libs.spring.boot.starter.logging)
    implementation(libs.spring.boot.starter.web)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.1")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")
}
