package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.computercraft.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.forged.MasonAutomataCorePeripheral

class MasonAutomata: ClockwiseAnimatedTurtleUpgrade<MasonAutomataCorePeripheral>(
    ID, ItemStack(Items.MASON_AUTOMATA_CORE)
) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "mason_automata");
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): MasonAutomataCorePeripheral {
        return MasonAutomataCorePeripheral(turtle,  side, (craftingItem.item as BaseAutomataCore).coreTier)
    }
}