package site.siredvin.turtlematic.data

import net.minecraft.data.PackOutput
import site.siredvin.peripheralium.data.language.LanguageProvider
import site.siredvin.peripheralium.data.language.toTurtleTranslationKey
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
        val baseStream = super.getExpectedKeys()
        val moreStream = Stream.of(
            extraExpectedKeys.stream(),
            TurtlematicPlatform.turtleUpgrades.stream().map { it.toTurtleTranslationKey() },
        ).flatMap { it }
        return Stream.concat(baseStream, moreStream)
    }
}
