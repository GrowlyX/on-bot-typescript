dependencies {
    api(kotlin("reflect"))
    api(kotlin("script-runtime"))

    runtimeOnly(kotlin("scripting-jsr223"))
    api(kotlin("compiler-embeddable"))

    compileOnly(fileTree("ftc-libs"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.reflections:reflections:0.10.2")

    runtimeOnly(kotlin("scripting-compiler-embeddable"))

    api(project(":frontend"))
    api(project(":backend"))

    testImplementation("junit:junit:4.12")
    testImplementation(fileTree("ftc-libs"))
}
