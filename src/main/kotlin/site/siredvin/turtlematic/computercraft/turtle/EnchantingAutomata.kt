package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.computercraft.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.forged.EnchantingAutomataCorePeripheral

class EnchantingAutomata: ClockwiseAnimatedTurtleUpgrade<EnchantingAutomataCorePeripheral>(
    ID, ItemStack(Items.ENCHANTING_AUTOMATA_CORE)
) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "enchanting_automata");
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): EnchantingAutomataCorePeripheral {
        return EnchantingAutomataCorePeripheral(turtle,  side, (craftingItem.item as BaseAutomataCore).coreTier)
    }
}