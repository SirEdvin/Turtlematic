package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.AutomataCorePeripheral
import site.siredvin.turtlematic.integrations.computercraft.peripheral.HusbandryAutomataCorePeripheral

class HusbandryAutomata: ClockwiseAnimatedTurtleUpgrade<HusbandryAutomataCorePeripheral>(
    ID, ItemStack(Items.HUSBANDRY_AUTOMATA_CORE)
) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "husbandry_automata")
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): HusbandryAutomataCorePeripheral {
        return HusbandryAutomataCorePeripheral(turtle,  side)
    }
}