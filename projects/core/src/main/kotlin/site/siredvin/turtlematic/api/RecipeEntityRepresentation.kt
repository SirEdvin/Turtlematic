package site.siredvin.turtlematic.api

import net.minecraft.network.chat.Component
import site.siredvin.turtlematic.data.ModTooltip

data class RecipeEntityRepresentation(val consumedCount: Int, val requiredCount: Int, val name: String) {

    val leftCount: Int
        get() = requiredCount - consumedCount

    fun toComponent(): Component {
        return ModTooltip.CONSUMED_ENTITIES_RECORD.format(consumedCount, requiredCount, name)
    }
}
