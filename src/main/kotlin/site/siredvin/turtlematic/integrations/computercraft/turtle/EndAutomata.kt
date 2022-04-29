package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.automatas.EndAutomataCorePeripheral

class EndAutomata: ClockwiseAnimatedTurtleUpgrade<EndAutomataCorePeripheral>(
    ID, ItemStack(Items.END_AUTOMATA_CORE)
) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "end_automata")
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): EndAutomataCorePeripheral {
        return EndAutomataCorePeripheral(turtle, side)
    }
}