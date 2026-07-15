@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.root") version "0.8.18"
    id("site.siredvin.release") version "0.8.18"
}

tasks.register("gameTest") {
    group = "verification"
    description = "Runs Turtlematic GameTests on Forge and Fabric."
    dependsOn(":forge:runGameTestServer", ":fabric:runTurtlematicGameTest")
}

subprojectShaking {
    withKotlin.set(true)
    kotlinVersion.set("2.0.0")
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    setupSubproject(this)
}

githubShaking {
    modBranch.set("1.20")
    useForgeJarJar.set(true)
    shake()
}

repositories {
    mavenCentral()
}
