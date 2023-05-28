import java.util.function.Consumer

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("peripheralium.root") version "0.2.5"
}

subprojectShaking {
    withKotlin.set(true)
}

val setupSubproject = subprojectShaking::setupSubproject

subprojects {
    setupSubproject(this)
}

repositories {
    mavenCentral()
}
