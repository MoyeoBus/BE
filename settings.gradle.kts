import java.nio.file.FileVisitOption
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import kotlin.io.path.absolute

pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


rootProject.name = "MoyeoBus"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositories { mavenCentral() }
}

fun includeModulesFrom(vararg entryPointNames: String) {
    entryPointNames.forEach { entryPointName ->
        val entryPoint = rootDir.resolve(entryPointName).toPath()

        // 디렉토리가 존재하지 않으면 스킵
        if (!Files.exists(entryPoint)) {
            println("Skipping non-existent directory: $entryPointName")
            return@forEach
        }

        val foundBuildScriptPaths = mutableListOf<java.nio.file.Path>()
        Files.walkFileTree(
            entryPoint,
            setOf(FileVisitOption.FOLLOW_LINKS),
            Integer.MAX_VALUE,
            FileVisitor("build.gradle.kts", foundBuildScriptPaths),
        )

        val modules =
            foundBuildScriptPaths
                .map { it.parent.absolute().toString() }
                .map { it.replace("${rootDir.toPath()}${File.separator}", "") }
                .map { it.replace(File.separator, ":") }
                .toList()

        println("Found modules in $entryPointName: $modules")

        include(*modules.toTypedArray())

        val moduleGroups =
            Files
                .list(entryPoint)
                .map { it.fileName.toString() }
                .map { "$entryPointName:$it" }
                .toList()

        include(*moduleGroups.toTypedArray())
    }
}

class FileVisitor(
    private val fileNameToSearch: String,
    private val foundBuildScriptPaths: MutableList<java.nio.file.Path>,
) : SimpleFileVisitor<java.nio.file.Path>() {
    override fun visitFile(
        file: java.nio.file.Path,
        attrs: java.nio.file.attribute.BasicFileAttributes,
    ): FileVisitResult {
        if (file.fileName.toString() == fileNameToSearch) {
            foundBuildScriptPaths.add(file.toAbsolutePath())
        }
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(
        file: java.nio.file.Path,
        exc: java.io.IOException,
    ): FileVisitResult {
        println("Error accessing file: $file - ${exc.message}")
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(
        dir: java.nio.file.Path,
        exc: java.io.IOException?,
    ): FileVisitResult = FileVisitResult.CONTINUE

    override fun preVisitDirectory(
        dir: java.nio.file.Path,
        attrs: java.nio.file.attribute.BasicFileAttributes,
    ): FileVisitResult = FileVisitResult.CONTINUE
}

// modules와 modules-scheduler 둘 다 포함
includeModulesFrom("modules", "modules-scheduler", "modules-websocket")