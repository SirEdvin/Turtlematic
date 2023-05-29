package site.siredvin.turtlematic.utils

import net.minecraftforge.fml.ModList
import site.siredvin.turtlematic.ForgeTurtlematic
import site.siredvin.turtlematic.TurtlematicCore
import java.util.*

object Platform {
    fun maybeLoadIntegration(modid: String, path: String = "Integration"): Optional<Any> {
        val modPresent = ModList.get().isLoaded(modid)
        if (modPresent) {
            TurtlematicCore.LOGGER.info("Loading integration for $modid")
            return maybeLoadIntegration("$modid.$path")
        } else {
            TurtlematicCore.LOGGER.info("Mod $modid is not present, skip loading integration")
        }
        return Optional.empty()
    }

    private fun maybeLoadIntegration(path: String): Optional<Any> {
        return try {
            val clazz =
                Class.forName(ForgeTurtlematic::class.java.getPackage().name + ".integrations." + path)
            Optional.of(clazz.newInstance())
        } catch (ignored: InstantiationException) {
            Optional.empty()
        } catch (ignored: IllegalAccessException) {
            Optional.empty()
        } catch (ignored: ClassNotFoundException) {
            Optional.empty()
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }
}
