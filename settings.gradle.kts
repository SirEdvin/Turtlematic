pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://repo.repsy.io/siredvin/default") {
            name = "Repsy"
            content { includeGroupByRegex("site\\.siredvin.*") }
        }
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.parchmentmc.org/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:${requested.version}")
            }
        }
    }
}

val minecraftVersion: String by settings
rootProject.name = "Turtlematic $minecraftVersion"

include(":core")
include(":forge")
include(":fabric")
include(":typed-peripheral-turtlematic")


for (project in rootProject.children) {
    project.projectDir = file("projects/${project.name}")
}
