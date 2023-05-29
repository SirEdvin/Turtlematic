package site.siredvin.turtlematic.api

import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.util.camelToSnakeCase

interface PeripheralConfiguration {
    val TYPE: String
    val UPGRADE_ID: ResourceLocation
        get() = ResourceLocation(TurtlematicCore.MOD_ID, TYPE.camelToSnakeCase())
}
