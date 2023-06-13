package site.siredvin.turtlematic.data

import net.minecraft.data.PackOutput
import site.siredvin.peripheralium.data.language.LanguageProvider
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.stream.Stream

abstract class ModLanguageProvider(
    output: PackOutput,
    locale: String,
) : LanguageProvider(output, TurtlematicCore.MOD_ID, locale, TurtlematicPlatform.holder, *ModText.values(), *ModTooltip.values()) {
    companion object {
        private val extraExpectedKeys: MutableList<String> = mutableListOf()

        fun addExpectedKey(key: String) {
            extraExpectedKeys.add(key)
        }
    }

    override fun getExpectedKeys(): Stream<String> {
        return Stream.concat(super.getExpectedKeys(), extraExpectedKeys.stream())
    }
}