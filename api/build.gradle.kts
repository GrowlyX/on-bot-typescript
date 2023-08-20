apply(from = "../build.ftc.dependencies.gradle")

dependencies {
    api(kotlin("stdlib"))
    api(kotlin("reflect"))
    api(kotlin("script-runtime"))
    api(kotlin("compiler-embeddable"))

    runtimeOnly(kotlin("scripting-compiler-embeddable"))

    api(project(":frontend"))
    api(project(":server"))
}
