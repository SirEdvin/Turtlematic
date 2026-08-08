import org.gradle.api.artifacts.ExternalModuleDependency

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("site.siredvin.publishing")
    id("site.siredvin.mod-publishing")
    id("site.siredvin.forge")
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

forgeShaking {
    commonProjectName.set("core")
    useAT.set(true)
    useMixins.set(true)
    useJarJar.set(true)
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
    mavenLocal()
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
    implementation(libs.bundles.forge.raw)
    libs.bundles.forge.cc.get().map { implementation(fg.deobf(it)) }
    libs.bundles.forge.include.get().map { implementation(fg.deobf(it)) }
    jarJar(libs.bundles.forge.jjar) {
        isTransitive = false
    }

    libs.bundles.externalMods.forge.runtime.get().map { runtimeOnly(fg.deobf(it)) }

    libs.bundles.externalMods.forge.integrations.full.get().map { compileOnly(fg.deobf(it)) }
    libs.bundles.externalMods.forge.integrations.active.get().map { runtimeOnly(fg.deobf(it)) }
    libs.bundles.externalMods.forge.integrations.activedep.get().map { runtimeOnly(fg.deobf(it)) }

    listOf(
        "site.siredvin:testiarium-forge-1.20.1:0.1.1",
        "site.siredvin:testiarium-forge-1.20.1:0.1.1:test-mod@jar",
        "site.siredvin:testiarium-forge-1.20.1:0.1.1:cct-test-mod@jar",
    ).forEach { notation ->
        add(
            testMod.implementationConfigurationName,
            fg.deobf((project.dependencies.create(notation) as ExternalModuleDependency).apply { isTransitive = false }),
        )
    }
}

minecraft {
    runs {
        create("gameTestServer") {
            workingDirectory(file("run/turtlematic-gametest"))
            property("forge.enabledGameTestNamespaces", "turtlematic_testmod")
            property("testiarium.tags", "turtlematic")
            property("testiarium.structures", project(":core").layout.buildDirectory.dir("resources/testMod/gameteststructures").get().asFile.absolutePath)
            property("testiarium.cct-fixtures", project(":core").file("src/testMod/resources/computer").absolutePath)
            property("testiarium.gametest-report", gameTestXmlReport.get().asFile.absolutePath)
            jvmArgs("-ea")
            args("--nogui")
            mods {
                create("turtlematic") {
                    source(sourceSets.main.get())
                }
                create("turtlematic_testmod") {
                    source(testMod)
                    source(project(":core").sourceSets["testMod"])
                }
            }
        }
    }
}

tasks.withType<JavaExec>().matching { it.name == "runGameTestServer" }.configureEach {
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
}

modPublishing {
    output.set(tasks.jarJar)
    requiredDependencies.set(
        listOf(
            "cc-tweaked",
            "kotlin-for-forge",
        ),
    )
    shake()
}
