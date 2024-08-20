package site.siredvin.turtlematic.api

import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.util.camelToSnakeCase

interface PeripheralConfiguration {
    val type: String
    val upgradeID: ResourceLocation
        get() = ResourceLocation(TurtlematicCore.MOD_ID, type.camelToSnakeCase())
}
