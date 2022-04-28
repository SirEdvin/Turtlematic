package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.TurtlematicClient
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.CreativeChestPeripheral

class CreativeChestTurtle(
): PeripheralTurtleUpgrade<CreativeChestPeripheral>(ID, ItemStack(Items.CREATIVE_CHEST)) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "creative_chest")
    }

    override val leftModel: ModelResourceLocation
        get() = TurtlematicClient.getLeftTurtleUpgradeModel(ID)

    override val rightModel: ModelResourceLocation
        get() = TurtlematicClient.getRightTurtleUpgradeModel(ID)

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): CreativeChestPeripheral {
        return CreativeChestPeripheral(TurtlePeripheralOwner(turtle, side))
    }
}