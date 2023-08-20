dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))
    api(kotlin("script-runtime"))
    api(kotlin("compiler-embeddable"))

    compileOnly(fileTree("ftc-libs"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    runtimeOnly(kotlin("scripting-compiler-embeddable"))

    api(project(":frontend"))
    api(project(":server"))
}
