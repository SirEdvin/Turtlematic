package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import site.siredvin.lib.util.convertColors
import site.siredvin.turtlematic.Turtlematic

fun itemTooltip(descriptionId: String): Component {
    val lastIndex = descriptionId.lastIndexOf('.')
    return TranslatableComponent(
        String.format(
            "%s.tooltip.%s",
            descriptionId.substring(0, lastIndex).replaceFirst("^block".toRegex(), "item"),
            descriptionId.substring(lastIndex + 1)
        )
    )
}

fun itemExtra(descriptionId: String, extra: String): Component {
    val lastIndex = descriptionId.lastIndexOf('.')
    return TranslatableComponent(
        String.format(
            "%s.extra.%s.%s",
            descriptionId.substring(0, lastIndex).replaceFirst("^block".toRegex(), "item"),
            descriptionId.substring(lastIndex + 1), extra
        )
    )
}

fun itemExtra(descriptionId: String, extra: String, vararg args: Any): Component {
    val lastIndex = descriptionId.lastIndexOf('.')
    return TranslatableComponent(
        String.format(
            "%s.extra.%s.%s",
            descriptionId.substring(0, lastIndex).replaceFirst("^block".toRegex(), "item"),
            descriptionId.substring(lastIndex + 1), extra
        ),
        *args
    )
}

fun turtleAdjective(name: String?): String? {
    return java.lang.String.format("turtle.%s.%s", Turtlematic.MOD_ID, name)
}

fun pocketAdjective(name: String?): String? {
    return java.lang.String.format("pocket.%s.%s", Turtlematic.MOD_ID, name)
}

fun text(name: String): Component {
    return TranslatableComponent(String.format("text.%s.%s", Turtlematic.MOD_ID, name))
}

fun text(name: String, vararg args: Any): Component {
    return TranslatableComponent(String.format("text.%s.%s", Turtlematic.MOD_ID, name), *args)
}
