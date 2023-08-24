import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.gradle.ext.Gradle

plugins {
    `maven-publish`
    java
    kotlin("jvm") version "1.9.0"
    kotlin("kapt") version "1.9.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.6"
}

allprojects {
    group = "io.liftgate.ftc.scripting"
    version = "0.1.2-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "org.jetbrains.gradle.plugin.idea-ext")
    apply(plugin = "maven-publish")

    dependencies {
        compileOnly(kotlin("stdlib"))
    }

    kotlin {
        jvmToolchain(jdkVersion = 11)
    }

    tasks.withType<ShadowJar> {
        archiveClassifier.set("")
        archiveFileName.set(
            "ftcscript-${project.name}.jar"
        )
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            // for compat purposes
            javaParameters = true
            jvmTarget = "11"
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.fork()
        options.encoding = "UTF-8"
    }

    publishing {
        repositories {
            configureLiftgateRepository()
        }

        publications {
            register(
                name = "mavenJava",
                type = MavenPublication::class,
                configurationAction = shadow::component
            )
        }
    }

    tasks["build"]
        .dependsOn(
            "publishMavenJavaPublicationToMavenLocal"
        )

    idea {
        module {
            isDownloadSources = true
        }
    }
}

fun RepositoryHandler.configureLiftgateRepository()
{
    val contextUrl = runCatching {
        property("liftgate_artifactory_contextUrl")
    }.getOrNull() ?: run {
        println("Skipping Artifactory configuration.")
        return
    }

    maven("$contextUrl/opensource") {
        name = "liftgate"

        credentials {
            username = property("liftgate_artifactory_user").toString()
            password = property("liftgate_artifactory_password").toString()
        }
    }
}

idea {
    project {
        settings {
            runConfigurations {
                create<Gradle>("Build All Modules") {
                    setProject(project)
                    scriptParameters = "clean build"
                }
            }
        }
    }
}
