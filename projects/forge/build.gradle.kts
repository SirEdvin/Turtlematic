import net.darkhax.curseforgegradle.TaskPublishCurseForge
import net.neoforged.moddevgradle.legacyforge.dsl.ObfuscationExtension
import org.gradle.api.artifacts.ExternalModuleDependency

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("net.neoforged.moddev.legacyforge")
    id("site.siredvin.publishing")
    id("site.siredvin.mod-publishing")
}

val modVersion: String by extra
val minecraftVersion: String by extra
val modBaseName: String by extra
val gameTestXmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.xml")
val gameTestHtmlReport = layout.buildDirectory.file("test-results/turtlematic-gametest.html")
val commonProject = project(":core")

baseShaking {
    projectPart.set("forge")
    integrationRepositories.set(true)
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

legacyForge {
    enable { forgeVersion = "${project.property("minecraftVersion")}-${libs.versions.forge.asProvider().get()}" }
    parchment {
        minecraftVersion = libs.versions.parchmentMc.get()
        mappingsVersion = libs.versions.parchment.get()
    }
    validateAccessTransformers.set(true)
    mods {
        create("turtlematic") { sourceSet(sourceSets.main.get()) }
        create("turtlematic_testmod") {
            sourceSet(testMod)
            sourceSet(commonProject.sourceSets["testMod"])
        }
    }
    runs {
        create("client") {
            client()
            gameDirectory = file("run")
            loadedMods.add(mods.named("turtlematic"))
        }
        create("server") {
            server()
            gameDirectory = file("run/server")
            programArgument("--nogui")
            loadedMods.add(mods.named("turtlematic"))
        }
        create("data") {
            data()
            gameDirectory = file("run")
            programArguments.addAll(
                "--mod", "turtlematic", "--all",
                "--output", file("src/generated/resources").absolutePath,
                "--existing", commonProject.file("src/main/resources").absolutePath,
                "--existing", file("src/main/resources").absolutePath,
            )
            loadedMods.add(mods.named("turtlematic"))
        }
        create("gameTestServer") {
            type = "gameTestServer"
            sourceSet.set(testMod)
            gameDirectory = file("run/turtlematic-gametest")
            systemProperty("forge.enabledGameTestNamespaces", "turtlematic_testmod")
            systemProperty("testiarium.tags", if (providers.environmentVariable("CI").isPresent) "turtlematic" else "turtlematic,local")
            systemProperty("testiarium.structures", commonProject.layout.buildDirectory.dir("resources/testMod/gameteststructures").get().asFile.absolutePath)
            systemProperty("testiarium.cct-fixtures", commonProject.file("src/testMod/resources/computer").absolutePath)
            systemProperty("testiarium.gametest-report", gameTestXmlReport.get().asFile.absolutePath)
            jvmArgument("-ea")
            programArgument("--nogui")
            loadedMods.add(mods.named("turtlematic"))
            loadedMods.add(mods.named("turtlematic_testmod"))
        }
    }
}

val obfuscation = extensions.getByType<ObfuscationExtension>()
val modTestModImplementation = obfuscation.createRemappingConfiguration(configurations.getByName(testMod.implementationConfigurationName))

dependencies {
    compileOnly(project(":core")) {
        exclude(group = "cc.tweaked")
        exclude(group = "fuzs.forgeconfigapiport")
        exclude(group = "dan200.computercraft")
    }
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    implementation(libs.bundles.forge.raw)
    libs.bundles.forge.cc.get().map { add("modImplementation", it) }
    libs.bundles.forge.include.get().map { add("modImplementation", it) }
    jarJar(libs.bundles.forge.jjar) {
        isTransitive = false
    }

    libs.bundles.externalMods.forge.runtime.get().map { add("modRuntimeOnly", it) }

    libs.bundles.externalMods.forge.integrations.full.get().map { add("modCompileOnly", it) }
    libs.bundles.externalMods.forge.integrations.active.get().map { add("modRuntimeOnly", it) }
    libs.bundles.externalMods.forge.integrations.activedep.get().map { add("modRuntimeOnly", it) }

    val testiarium = "site.siredvin:testiarium-forge-$minecraftVersion:${libs.versions.testiarium.get()}"
    listOf(testiarium, "$testiarium:test-mod@jar", "$testiarium:cct-test-mod@jar").forEach { notation ->
        add(modTestModImplementation.name, (project.dependencies.create(notation) as ExternalModuleDependency).apply { isTransitive = false })
    }
}

tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileKotlin") { source(commonProject.sourceSets.main.get().allSource) }
tasks.named<JavaCompile>("compileJava") { source(commonProject.sourceSets.main.get().allSource) }

tasks.processResources {
    from(commonProject.sourceSets.main.get().resources)
    val versions = mapOf(
        "version" to project.version,
        "file" to mapOf("jarVersion" to project.version),
        "forgeVersion" to libs.versions.forge.asProvider().get(),
        "computercraftVersion" to libs.versions.cc.tweaked.get(),
        "tweakiumVersion" to libs.versions.tweakium.asProvider().get(),
        "broccoliumVersion" to libs.versions.broccolium.asProvider().get(),
        "peripheraliumVersion" to libs.versions.peripheralium.asProvider().get(),
    )
    inputs.properties(versions)
    filesMatching("META-INF/mods.toml") { expand(versions) }
    exclude(".cache")
}

tasks.jar { manifest.attributes["MixinConfigs"] = "turtlematic.mixins.json" }

extra["releaseJar"] = "reobfJar"

mixin {
    add(sourceSets.main.get(), "turtlematic.refmap.json")
    config("turtlematic.mixins.json")
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
    output.set(tasks.named<Jar>("reobfJar"))
    requiredDependencies.set(
        listOf(
            "cc-tweaked",
            "kotlin-for-forge",
        ),
    )
    shake()
}

// Buildenv infers the loader from the ForgeGradle plugin, which this project no longer applies.
modrinth { loaders.add("forge") }

tasks.named<TaskPublishCurseForge>("publishCurseForge") {
    uploadArtifacts.forEach { it.addModLoader("Forge") }
}
