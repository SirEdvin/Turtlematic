@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.root") version "0.8.16"
    id("site.siredvin.release") version "0.8.16"
}

subprojectShaking {
    withKotlin.set(true)
    kotlinVersion.set("2.0.0")
    javaVersion.set(JavaVersion.VERSION_21)
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    setupSubproject(this)
}

githubShaking {
    modBranch.set("1.20")
    shake()
}

repositories {
    mavenCentral()
}
