dependencies {
    api(kotlin("reflect"))
    api(kotlin("script-runtime"))

    runtimeOnly(kotlin("scripting-jsr223"))
    api(kotlin("compiler-embeddable"))

    compileOnly(fileTree("ftc-libs"))
    // TODO: compat issues with Android? Do we need coroutines-android?
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    runtimeOnly(kotlin("scripting-compiler-embeddable"))

    api(project(":frontend"))
    api(project(":backend"))
}
