package site.siredvin.turtlematic.tags

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import site.siredvin.turtlematic.Turtlematic

object ItemTags {
    val CAPTURE_BLACKLIST = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation(Turtlematic.MOD_ID, "capture_blacklist"))
    val ENCHANTMENT_POWER_PROVIDER = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation(Turtlematic.MOD_ID, "enchantment_power_provider"))
}