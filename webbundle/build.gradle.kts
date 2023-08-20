plugins {
    application
}

dependencies {
    implementation(project(":frontend"))
    implementation(project(":backend"))
}

application {
    mainClass.set("io.liftgate.ftc.scripting.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks["build"]
    .dependsOn(
        "publishMavenJavaPublicationToMavenLocal"
    )
