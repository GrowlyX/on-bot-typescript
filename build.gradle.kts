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
    version = "1.0-SNAPSHOT"

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
        jvmToolchain(jdkVersion = 8)
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
            jvmTarget = "1.8"
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-parameters")
        options.fork()
        options.encoding = "UTF-8"
    }

    publishing {
        publications {
            register(
                name = "mavenJava",
                type = MavenPublication::class,
                configurationAction = shadow::component
            )
        }
    }

    idea {
        module {
            isDownloadSources = true
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
