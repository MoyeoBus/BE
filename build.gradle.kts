buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-gradle-plugin:11.7.2")
        classpath("org.flywaydb:flyway-mysql:11.7.2")
        classpath("com.mysql:mysql-connector-j:8.3.0")
    }
}

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
    id("org.flywaydb.flyway") version "11.7.2"
    kotlin("plugin.jpa") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

group = "com.moyeobus"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

// 프로젝트 전역 설정
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    configurations.all {
        resolutionStrategy {
            force("com.google.protobuf:protobuf-java:3.21.9")
        }
    }

    group = "com.moyeobus"
    version = "0.0.1-SNAPSHOT"

    java { toolchain { languageVersion = JavaLanguageVersion.of(22) } }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
            // Java 22 툴체인을 사용하되, 바이트코드 타겟은 21로 고정(컴파일러 호환성)
            @Suppress("UnstableApiUsage")
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    // Java 컴파일러도 release 21로 정렬하여 Kotlin과 타겟 일치
    tasks.withType<JavaCompile>().configureEach { options.release.set(21) }

    dependencies {
        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        // jackson
        implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // test
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("io.mockk:mockk:1.13.5")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("com.appmattus.fixture:fixture:1.2.0")
    }
    // Ktlint 설정
    ktlint {
        version.set("0.45.2")
        debug.set(false)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(true) // 형식상 문제 있어도 일단 진행
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}


// bootstrap 하위 모듈들에 플러그인 적용
subprojects.filter { it.path.contains(":bootstrap:") }.forEach { project ->
    project.apply(plugin = "org.springframework.boot")
    project.apply(plugin = "io.spring.dependency-management")
    project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}

// bootstrap 모듈에 플러그인 적용
subprojects.filter { it.path.contains(":bootstrap") }.forEach { project ->
    project.apply(plugin = "org.springframework.boot")
    project.apply(plugin = "io.spring.dependency-management")
    project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}

// application 하위 모듈들에 플러그인 적용
subprojects.filter { it.path.contains(":application") }.forEach { project ->
    project.apply(plugin = "org.springframework.boot")
    project.apply(plugin = "io.spring.dependency-management")
    project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}

// infrastructure 하위 모듈들에 플러그인 적용
subprojects.filter { it.path.contains(":infrastructure:") }.forEach { project ->
    project.apply(plugin = "org.springframework.boot")
    project.apply(plugin = "io.spring.dependency-management")
    project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}

// common 모듈에 플러그인 적용
subprojects.filter { it.path.contains(":common") }.forEach { project ->
    project.apply(plugin = "org.springframework.boot")
    project.apply(plugin = "io.spring.dependency-management")
    project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
}

// domain POJO
subprojects.filter { it.path.contains(":domain") }.forEach { project ->

}

subprojects {
    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }
}

flyway {
    url = "jdbc:mysql://localhost:3306/moyeobus?serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
    user = "root"
    password = "keypass"
    schemas = arrayOf("moyeobus")
    locations = arrayOf("filesystem:modules/infrastructure/persistence/src/main/resources/db/migration")
    cleanDisabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}
