package site.siredvin.lib.api

import net.minecraftforge.common.ForgeConfigSpec
import java.util.*
import java.util.stream.Collectors

interface IConfigHandler {
    val name: String

    val settingsPostfix: String
        get() = ""

    fun addToConfig(builder: ForgeConfigSpec.Builder)

    fun settingsName(): String {
        val startName = Arrays.stream(name.lowercase(Locale.getDefault()).split("_").toTypedArray())
            .map { s: String ->
                s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1).lowercase(Locale.getDefault())
            }
            .collect(Collectors.joining()) + settingsPostfix
        return startName.substring(0, 1).lowercase(Locale.getDefault()) + startName.substring(1)
    }
}