package site.siredvin.turtlematic.tags

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import site.siredvin.turtlematic.Turtlematic

object EntityTags {
    val CAPTURE_BLACKLIST = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, ResourceLocation(Turtlematic.MOD_ID, "capture_blacklist"))
}