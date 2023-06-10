package site.siredvin.turtlematic.data

import site.siredvin.peripheralium.data.language.TextRecord
import site.siredvin.turtlematic.TurtlematicCore

enum class ModText: TextRecord{
    CREATIVE_TAB,
    CORE_FEED_BY_PLAYER,
    ;

    override val textID: String by lazy {
        String.format("text.%s.%s", TurtlematicCore.MOD_ID, name.lowercase())
    }
}