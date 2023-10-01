import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compileOnly(fileTree("ftc-libs"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    api(project(":frontend"))
    api(project(":backend"))

    testImplementation("junit:junit:4.12")
    testImplementation(fileTree("ftc-libs"))
}

tasks {
    withType<ShadowJar> {
        exclude("**/org/slf4j/**")
        exclude("**/com/google/gson/**")
        exclude("**/META-INF/maven/com.google.code.gson/gson/**")
    }
}
