package site.siredvin.turtlematic.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import site.siredvin.turtlematic.TurtlematicCore

object EntityTags {
    val CAPTURE_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation(TurtlematicCore.MOD_ID, "capture_blacklist"))
    val ANIMAL = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation(TurtlematicCore.MOD_ID, "animal"))
}
