package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.turtle.PeripheralToolTurtleUpgrade
import site.siredvin.lib.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.SoulScrapperPeripheral

class SoulScrapperTurtle(
): PeripheralTurtleUpgrade<SoulScrapperPeripheral>(ID, ItemStack(Items.SOUL_SCRAPPER)) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "soul_scrapper")
    }

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): SoulScrapperPeripheral {
        return SoulScrapperPeripheral(TurtlePeripheralOwner(turtle, side))
    }
}