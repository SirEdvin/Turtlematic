import site.siredvin.peripheralium.gradle.mavenDependencies

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.publishing")
    id("site.siredvin.mod-publishing")
    id("site.siredvin.neoforge")
}

val modVersion: String by extra
val minecraftVersion: String by extra
val modBaseName: String by extra
val gameTestXmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.xml")
val gameTestHtmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.html")

baseShaking {
    projectPart.set("forge")
    integrationRepositories.set(true)
    shake()
}

neoforgeShaking {
    commonProjectName.set("core")
    useAT.set(true)
    extraVersionMappings.set(
        mapOf(
            "computercraft" to "cc-tweaked",
            "tweakium" to "tweakium",
            "broccolium" to "broccolium",
            "peripheralium" to "peripheralium",
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

repositories {
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content {
            includeGroup("thedarkcolour")
        }
    }
    // location of the maven that hosts JEI files since January 2023
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }
    // Integration dependencies

    maven {
        name = "KliKli Dev Repsy Maven (Occultism)"
        url = uri("https://repo.repsy.io/mvn/klikli-dev/mods")
        content {
            includeGroup("com.klikli_dev")
        }
    }

    maven {
        name = "Curios Maven"
        url = uri("https://maven.theillusivec4.top/")
        content {
            includeGroup("top.theillusivec4.curios")
        }
    }

    maven {
        name = "SBL Maven"
        url = uri("https://dl.cloudsmith.io/public/tslat/sbl/maven/")
        content {
            includeGroup("net.tslat.smartbrainlib")
        }
    }
    maven {
        name = "Geckolib Maven"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroup("software.bernie.geckolib")
        }
    }
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.forge.raw)
    implementation(libs.bundles.forge.cc)
    implementation(libs.bundles.forge.include)
    jarJar(libs.bundles.forge.jjar) {
        isTransitive = false
    }

    runtimeOnly(libs.bundles.externalMods.forge.runtime)

    compileOnly(libs.bundles.externalMods.forge.integrations.full)
    runtimeOnly(libs.bundles.externalMods.forge.integrations.active)
    runtimeOnly(libs.bundles.externalMods.forge.integrations.activedep)

    listOf(
        "site.siredvin:testiarium-forge-1.21.1:0.1.1",
        "site.siredvin:testiarium-forge-1.21.1:0.1.1:test-mod@jar",
        "site.siredvin:testiarium-forge-1.21.1:0.1.1:cct-test-mod@jar",
    ).forEach { notation ->
        add(testMod.implementationConfigurationName, notation) {
            isTransitive = false
        }
    }
}

neoForge {
    val turtlematic = mods.named("turtlematic")
    val turtlematicTestMod by mods.registering {
        sourceSet(testMod)
        sourceSet(project(":core").sourceSets["testMod"])
    }
    runs {
        configureEach {
            if (name != "gameTestServer") {
                loadedMods.set(listOf(turtlematic.get()))
            }
        }
        register("gameTestServer") {
            type = "gameTestServer"
            sourceSet = testMod
            gameDirectory = file("run/turtlematic-gametest")
            systemProperty("testiarium.tags", "turtlematic")
            systemProperty("testiarium.structures", project.project(":core").layout.buildDirectory.dir("resources/testMod/gameteststructures").get().asFile.absolutePath)
            systemProperty("testiarium.cct-fixtures", project.project(":core").file("src/testMod/resources/computer").absolutePath)
            systemProperty("testiarium.gametest-report", gameTestXmlReport.get().asFile.absolutePath)
            jvmArgument("-ea")
            programArgument("--nogui")
            loadedMods.add(turtlematic.get())
            loadedMods.add(turtlematicTestMod.get())
        }
    }
}

tasks.named<JavaExec>("runGameTestServer") {
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

publishingShaking {
    shake()
    project.publishing {
        publications {
            named<MavenPublication>("maven") {
                mavenDependencies {
                    exclude(dependencies.create("site.siredvin:"))
                    exclude(libs.jei.forge.get())
                }
            }
        }
    }
}

modPublishing {
    output.set(tasks.jar)
    requiredDependencies.set(
        listOf(
            "cc-tweaked",
            "kotlin-for-forge",
        ),
    )
    shake()
}
