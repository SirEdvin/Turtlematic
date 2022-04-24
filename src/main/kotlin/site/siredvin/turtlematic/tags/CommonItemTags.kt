package site.siredvin.turtlematic.tags

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

object CommonItemTags {
    val REDSTONE_BLOCK = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "redstone_blocks"))
    val IRON_BLOCK = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "iron_blocks"))
    val IRON_INGOT = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "iron_ingots"))
    val DIAMOND = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "diamonds"))
    val EMERALD = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "emeralds"))
    val REDSTONE = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "redstone_dusts"))
    val WOOD_STICK = TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation("c", "wood_sticks"))
}