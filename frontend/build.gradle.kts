plugins {
    id("org.siouan.frontend-jdk11") version "6.0.0"
    java
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

frontend {
    nodeVersion.set("16.15.0")
    cleanScript.set("run clean")
    installScript.set("install")
    assembleScript.set("run build")
}

tasks {
    jar {
        dependsOn("assembleFrontend")
        from("$buildDir/dist")
        into("static")
    }
}
