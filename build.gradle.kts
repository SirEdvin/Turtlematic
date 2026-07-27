plugins {
    java
    alias(libs.plugins.loom) apply false
    id("site.siredvin.root") version "0.9.0"
    id("site.siredvin.release") version "0.9.0"
}

tasks.register("gameTest") {
    group = "verification"
    description = "Runs Turtlematic GameTests on NeoForge and Fabric."
    dependsOn(":forge:runGameTestServer", ":fabric:runTurtlematicGameTest")
}

subprojectShaking {
    withKotlin.set(true)
    javaVersion.set(JavaVersion.VERSION_21)
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    if (name != "typed-peripheral-turtlematic") {
        setupSubproject(this)
    }
}

githubShaking {
    modBranch.set("1.21")
    useForgeJarJar.set(true)
    shake()
}

repositories {
    mavenCentral()
}
