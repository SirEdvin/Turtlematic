package site.siredvin.lib.util

import dan200.computercraft.api.ComputerCraftAPI
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

object BlockUtil {
    val TURTLE_NORMAL: Block
        get() = Registry.BLOCK.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "turtle_normal"))

    val TURTLE_ADVANCED: Block
        get() = Registry.BLOCK.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "turtle_advanced"))
}