package site.siredvin.turtlematic.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.base.BaseItem

fun <T: BaseItem> T.register(name: String): T {
    Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, name), this)
    return this
}