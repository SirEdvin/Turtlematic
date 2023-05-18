package site.siredvin.turtlematic.util

import net.minecraft.resources.ResourceLocation

fun ResourceLocation.toNetherite(): ResourceLocation {
    return ResourceLocation(this.namespace, "netherite_${this.path}")
}

fun ResourceLocation.toStarbound(): ResourceLocation {
    return ResourceLocation(this.namespace, "starbound_${this.path}")
}

fun ResourceLocation.toCreative(): ResourceLocation {
    return ResourceLocation(this.namespace, "creative_${this.path}")
}