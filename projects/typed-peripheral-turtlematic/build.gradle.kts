import com.github.gradle.node.npm.task.NpmTask

plugins {
    base
    id("com.github.node-gradle.node") version "7.1.0"
}

node {
    version.set("22.14.0")
    download.set(true)
    nodeProjectDir.set(projectDir)
    workDir.set(rootProject.layout.projectDirectory.dir(".gradle/nodejs"))
    npmWorkDir.set(rootProject.layout.projectDirectory.dir(".gradle/npm"))
    npmInstallCommand.set("ci")
}

val typeScriptSources = fileTree(projectDir) {
    include("**/*.ts")
    exclude("node_modules/**")
}

val compileTypeScript by tasks.registering(NpmTask::class) {
    dependsOn(tasks.npmInstall)
    npmCommand.set(listOf("run", "build"))
    inputs.files("package.json", "package-lock.json", "tsconfig.json", typeScriptSources)
    outputs.files(typeScriptSources.files.flatMap { source ->
        val output = source.relativeTo(projectDir).invariantSeparatorsPath.removeSuffix(".ts")
        listOf(file("$output.d.ts"), file("$output.lua"))
    } + file("lualib_bundle.lua"))
}

tasks.assemble {
    dependsOn(compileTypeScript)
}

tasks.clean {
    delete(fileTree(projectDir) {
        include("**/*.d.ts", "**/*.lua")
    })
}
