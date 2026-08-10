@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.fabric")
    id("site.siredvin.publishing")
    id("site.siredvin.mod-publishing")
}

val modVersion: String by extra
val minecraftVersion: String by extra
val modBaseName: String by extra

baseShaking {
    projectPart.set("fabric")
    integrationRepositories.set(true)
    shake()
}

fabricShaking {
    commonProjectName.set("core")
    accessWidener.set(project(":core").file("src/main/resources/turtlematic.accesswidener"))
    createRefmap.set(true)
    extraVersionMappings.set(
        mapOf(
            "computercraft" to "cc-tweaked",
            "peripheralium" to "peripheralium",
            "tweakium" to "tweakium",
            "broccolium" to "broccolium",
        ),
    )
    shake()
}

val testMod = sourceSets.create("testMod") {
    compileClasspath += sourceSets.main.get().compileClasspath
    compileClasspath += sourceSets.main.get().output
    compileClasspath += project(":core").sourceSets["testMod"].output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    runtimeClasspath += sourceSets.main.get().output
    runtimeClasspath += project(":core").sourceSets["testMod"].output
}

net.fabricmc.loom.configuration.RemapConfigurations.setupForSourceSet(project, testMod)

val testiariumMainArtifacts = configurations.detachedConfiguration(
    project.dependencies.create("site.siredvin:testiarium-core-1.20.1:0.1.1"),
    project.dependencies.create("site.siredvin:testiarium-fabric-1.20.1:0.1.1"),
).apply {
    isTransitive = false
}

val testiariumTestModArtifacts = configurations.detachedConfiguration(
    project.dependencies.create("site.siredvin:testiarium-core-1.20.1:0.1.1:test-mod@jar"),
    project.dependencies.create("site.siredvin:testiarium-fabric-1.20.1:0.1.1:test-mod@jar"),
    project.dependencies.create("site.siredvin:testiarium-core-1.20.1:0.1.1:cct-test-mod@jar"),
).apply {
    isTransitive = false
}

val gameTestXmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.xml")
val gameTestHtmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.html")

loom {
    mods {
        register("turtlematic-testmod") {
            sourceSet(testMod)
        }
    }
    runs {
        create("turtlematicGameTest") {
            server()
            source(testMod)
            property("fabric-api.gametest", "true")
            property("fabric.debug.loadLate", "testiarium_testmod")
            property("testiarium.tags", if (providers.environmentVariable("CI").isPresent) "turtlematic" else "turtlematic,local")
            property("testiarium.structures", project(":core").layout.buildDirectory.dir("resources/testMod/gameteststructures").get().asFile.absolutePath)
            property("testiarium.cct-fixtures", project(":core").file("src/testMod/resources/computer").absolutePath)
            property("testiarium.gametest-report", gameTestXmlReport.get().asFile.absolutePath)
            vmArg("-ea")
            programArg("--nogui")
            runDir("run/turtlematic-gametest")
        }
    }
}

tasks.named<JavaExec>("runTurtlematicGameTest") {
    doFirst {
        delete(gameTestXmlReport, gameTestHtmlReport)
    }
    doLast {
        listOf(gameTestXmlReport.get().asFile, gameTestHtmlReport.get().asFile).forEach { report ->
            check(report.isFile && report.length() > 0) {
                "GameTest server did not produce report ${report.absolutePath}"
            }
        }
    }
}

repositories {
    mavenLocal()
    // location of the maven that hosts JEI files since January 2023
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }
    maven {
        name = "ModMenu maven"
        url = uri("https://maven.terraformersmc.com/releases")
        content {
            includeGroup("com.terraformersmc")
        }
    }
}

dependencies {
    modApi(libs.bundles.externalMods.fabric.integrations.api) {
        exclude("net.fabricmc.fabric-api")
    }

    modImplementation(libs.bundles.fabric.core)
    modImplementation(libs.bundles.fabric)
    modImplementation(libs.bundles.fabric.cc) {
        exclude("net.fabricmc.fabric-api")
        exclude("net.fabricmc", "fabric-loader")
        exclude("mezz.jei")
    }
    modImplementation(libs.bundles.fabric.include) {
        exclude("net.fabricmc.fabric-api")
        exclude("net.fabricmc", "fabric-loader")
        exclude("mezz.jei")
    }

    include(libs.bundles.fabric.include)

    modRuntimeOnly(libs.bundles.externalMods.fabric.runtime) {
        exclude("net.fabricmc.fabric-api")
        exclude("net.fabricmc", "fabric-loader")
    }

    libs.bundles.externalMods.fabric.integrations.full.get().map { modCompileOnly(it) }
    libs.bundles.externalMods.fabric.integrations.active.get().map { modRuntimeOnly(it) }
    libs.bundles.externalMods.fabric.integrations.activedep.get().map { modRuntimeOnly(it) }

    add("modTestModImplementation", libs.bundles.fabric.core)
    add("modTestModImplementation", files(testiariumMainArtifacts))
    add("modTestModImplementation", files(testiariumTestModArtifacts))
}

publishingShaking {
    shake()
}

modPublishing {
    output.set(tasks.remapJar)
    requiredDependencies.set(
        listOf(
            "cc-tweaked",
            "fabric-language-kotlin",
        ),
    )
    requiredDependenciesCurseforge.add("forge-config-api-port")
    requiredDependenciesModrinth.add("forge-config-api-port")
    shake()
}
