package site.siredvin.turtlematic.api

import net.minecraft.network.chat.Component
import site.siredvin.turtlematic.util.text

data class RecipeEntityRepresentation(val consumedCount: Int, val requiredCount: Int, val name: String) {

    val leftCount: Int
        get() = requiredCount - consumedCount

    fun toComponent(): Component {
        return text("consumed_entities_record", consumedCount, requiredCount, name)
    }
}
