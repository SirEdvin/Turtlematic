package site.siredvin.turtlematic.tags

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import site.siredvin.turtlematic.TurtlematicCore

object EntityTags {
    val CAPTURE_BLOCKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation(TurtlematicCore.MOD_ID, "capture_blocklist"))
    val AI_CONTROL_BLOCKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation(TurtlematicCore.MOD_ID, "ai_control_blocklist"))
    val ANIMAL = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation(TurtlematicCore.MOD_ID, "animal"))
}
