package site.siredvin.lib.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation

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

fun turtleAdjective(turtleID: ResourceLocation): String {
    return java.lang.String.format("turtle.%s.%s", turtleID.namespace, turtleID.path)
}

fun pocketAdjective(pocketID: ResourceLocation): String {
    return java.lang.String.format("pocket.%s.%s", pocketID.namespace, pocketID.path)
}

fun text(modID: String, name: String): Component {
    return TranslatableComponent(String.format("text.%s.%s", modID, name))
}

fun text(modID: String, name: String, vararg args: Any): Component {
    return TranslatableComponent(String.format("text.%s.%s", modID, name), *args)
}
