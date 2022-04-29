package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.lib.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.TurtlematicClient
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.misc.TurtleChatterPeripheral

class ChatterTurtle(
): PeripheralTurtleUpgrade<TurtleChatterPeripheral>(ID, ItemStack(Items.TURTLE_CHATTER)) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "chatter")
    }

    override val leftModel: ModelResourceLocation
        get() = TurtlematicClient.getLeftTurtleUpgradeModel(ID)

    override val rightModel: ModelResourceLocation
        get() = TurtlematicClient.getRightTurtleUpgradeModel(ID)

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): TurtleChatterPeripheral {
        return TurtleChatterPeripheral(TurtlePeripheralOwner(turtle, side))
    }
}