package site.siredvin.turtlematic.util

import net.minecraft.network.chat.TranslatableComponent
import site.siredvin.turtlematic.Turtlematic

fun itemTooltip(descriptionId: String): TranslatableComponent {
    val lastIndex = descriptionId.lastIndexOf('.')
    return TranslatableComponent(
        String.format(
            "%s.tooltip.%s",
            descriptionId.substring(0, lastIndex).replaceFirst("^block".toRegex(), "item"),
            descriptionId.substring(lastIndex + 1)
        )
    )
}

fun turtleAdjective(name: String?): String? {
    return java.lang.String.format("turtle.%s.%s", Turtlematic.MOD_ID, name)
}

fun pocketAdjective(name: String?): String? {
    return java.lang.String.format("pocket.%s.%s", Turtlematic.MOD_ID, name)
}