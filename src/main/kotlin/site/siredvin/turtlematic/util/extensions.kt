package site.siredvin.turtlematic.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items

fun <T: Item> T.register(name: String): T {
    Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, name), this)
    Items.ITEMS.add(this)
    return this
}