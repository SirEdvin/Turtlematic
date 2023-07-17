package site.siredvin.turtlematic.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import site.siredvin.turtlematic.TurtlematicCore

object ItemTags {
    val CAPTURE_BLOCKLIST = TagKey.create(Registries.ITEM, ResourceLocation(TurtlematicCore.MOD_ID, "capture_blocklist"))
    val ENCHANTMENT_POWER_PROVIDER = TagKey.create(Registries.ITEM, ResourceLocation(TurtlematicCore.MOD_ID, "enchantment_power_provider"))
    val MIMIC_BLOCKLIST = TagKey.create(Registries.BLOCK, ResourceLocation(TurtlematicCore.MOD_ID, "mimic_blocklist"))
}
