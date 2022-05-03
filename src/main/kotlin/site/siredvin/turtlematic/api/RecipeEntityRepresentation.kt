package site.siredvin.turtlematic.api

import net.minecraft.network.chat.Component
import site.siredvin.lib.util.text
import site.siredvin.turtlematic.Turtlematic

data class RecipeEntityRepresentation(val consumedCount: Int, val requiredCount: Int, val name: String) {

    val leftCount: Int
        get() = requiredCount - consumedCount

    fun toComponent(): Component {
        return text(Turtlematic.MOD_ID,"consumed_entities_record", consumedCount, requiredCount, name)
    }
}
