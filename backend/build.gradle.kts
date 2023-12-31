plugins {
    id("io.ktor.plugin") version "2.3.3"
}

val ktor_version: String by project
val logback_version: String by project

dependencies {
    implementation("io.apisense:rhino-android:1.2.0")
    implementation("io.liftgate.robotics.ts4j:ts4j-modern:2.3-SNAPSHOT")

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-cio-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test-junit"))
}
