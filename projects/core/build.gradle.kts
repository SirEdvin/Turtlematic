@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.vanilla")
    id("site.siredvin.publishing")
}

val modVersion: String by extra
val minecraftVersion: String by extra
val modBaseName: String by extra

baseShaking {
    projectPart.set("common")
    shake()
}

vanillaShaking {
    accessWideners.add("src/main/resources/turtlematic-common.accesswidener")
    accessWideners.add("src/main/resources/turtlematic.accesswidener")
    shake()
}

val testMod = sourceSets.create("testMod") {
    compileClasspath += sourceSets.main.get().compileClasspath
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    runtimeClasspath += sourceSets.main.get().output
}

val testiariumTestModArtifact = configurations.detachedConfiguration(
    dependencies.create("site.siredvin:testiarium-core-1.20.1:0.1.1:test-mod@jar"),
).apply {
    isTransitive = false
}

val testiariumCctTestModArtifact = configurations.detachedConfiguration(
    dependencies.create("site.siredvin:testiarium-core-1.20.1:0.1.1:cct-test-mod@jar"),
).apply {
    isTransitive = false
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(libs.bundles.cccommon)
    api(libs.bundles.apicommon)
    compileOnly(libs.mixin)
    add(testMod.implementationConfigurationName, libs.testiarium.core)
    add(testMod.implementationConfigurationName, files(testiariumTestModArtifact))
    add(testMod.implementationConfigurationName, files(testiariumCctTestModArtifact))
}

tasks.named<ProcessResources>(testMod.processResourcesTaskName) {
    from(provider { zipTree(testiariumTestModArtifact.singleFile) }) {
        include("gameteststructures/empty.snbt")
    }
}
