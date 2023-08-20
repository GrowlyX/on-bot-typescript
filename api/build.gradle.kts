dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))
    api(kotlin("script-runtime"))
    api(kotlin("compiler-embeddable"))

    compileOnly(fileTree("ftc-libs"))

    runtimeOnly(kotlin("scripting-compiler-embeddable"))

    api(project(":frontend"))
    api(project(":server"))
}
