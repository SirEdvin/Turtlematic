@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.root") version "0.9.2"
    id("site.siredvin.release") version "0.9.2"
}

tasks.register("gameTest") {
    group = "verification"
    description = "Runs Turtlematic server GameTests on Forge and Fabric plus Fabric client GameTests."
    dependsOn(
        ":forge:runGameTestServer",
        ":fabric:runTurtlematicGameTest",
        ":fabric:runTurtlematicClientGameTest",
    )
}

subprojectShaking {
    withKotlin.set(true)
    kotlinVersion.set("2.0.0")
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    if (name != "typed-peripheral-turtlematic") {
        setupSubproject(this)
    }
}

githubShaking {
    modBranch.set("1.20")
    useForgeJarJar.set(true)
    shake()
}

repositories {
    mavenCentral()
}
