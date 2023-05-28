@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("peripheralium.vanilla")
    id("peripheralium.publishing")
}

val modVersion: String by extra
val minecraftVersion: String by extra
val modBaseName: String by extra

baseShaking {
    projectPart.set("common")
    shake()
}

vanillaShaking {
    accessWideners.add("src/main/resources/turtlematic-common.accesswidener",)
    shake()
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(libs.bundles.cccommon)
    api(libs.bundles.apicommon)
    compileOnly(libs.mixin)
}