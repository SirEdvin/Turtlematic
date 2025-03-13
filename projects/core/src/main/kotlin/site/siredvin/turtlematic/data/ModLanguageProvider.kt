package site.siredvin.turtlematic.data

import net.minecraft.data.PackOutput
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.xplat.ModPlatform
import site.siredvin.tweakium.modules.data.ComputerLanguageProvider
import java.util.stream.Stream

abstract class ModLanguageProvider(
    output: PackOutput,
    locale: String,
) : ComputerLanguageProvider(output, TurtlematicCore.MOD_ID, locale, ModPlatform.holder, *ModText.entries.toTypedArray(), *ModTooltip.entries.toTypedArray()) {
    companion object {
        private val extraExpectedKeys: MutableList<String> = mutableListOf()

        fun addExpectedKey(key: String) {
            extraExpectedKeys.add(key)
        }
    }

    override fun getExpectedKeys(): Stream<String> = Stream.concat(super.getExpectedKeys(), extraExpectedKeys.stream())
}
