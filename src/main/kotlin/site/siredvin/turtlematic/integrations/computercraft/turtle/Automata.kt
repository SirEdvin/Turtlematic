package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.AutomataCorePeripheral

class Automata: ClockwiseAnimatedTurtleUpgrade<AutomataCorePeripheral>(
    ID, ItemStack(Items.AUTOMATA_CORE)
) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "automata")
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): AutomataCorePeripheral {
        return AutomataCorePeripheral(turtle,  side)
    }
}