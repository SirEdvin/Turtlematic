package site.siredvin.turtlematic.testmod

import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.storage.LevelResource
import site.siredvin.testiarium.cct.CctFixtureCommands
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.relativeTo

object TurtlematicComputerFixtures {
    fun importFiles(server: MinecraftServer) {
        CctFixtureCommands.importFiles(server)
        val source = server.getWorldPath(LevelResource.ROOT).resolve("computercraft/computer/1")
        // ponytail: Test fixtures use fixed computer IDs 1..12.
        (2..12).forEach { id ->
            Files.walk(source).use { paths ->
                paths.forEach { path ->
                    val target = source.resolveSibling(id.toString()).resolve(path.relativeTo(source).toString())
                    if (Files.isDirectory(path)) {
                        Files.createDirectories(target)
                    } else {
                        Files.createDirectories(target.parent)
                        Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING)
                    }
                }
            }
        }
    }
}
