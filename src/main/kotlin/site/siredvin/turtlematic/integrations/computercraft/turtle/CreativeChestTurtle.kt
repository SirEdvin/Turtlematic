package site.siredvin.turtlematic.integrations.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.integrations.computercraft.peripheral.CreativeChestPeripheral

class CreativeChestTurtle(
): PeripheralTurtleUpgrade<CreativeChestPeripheral>(ID, ItemStack(Items.CREATIVE_CHEST)) {
    companion object {
        val ID = ResourceLocation(Turtlematic.MOD_ID, "creative_chest")
        private val LEFT_MODEL = ModelResourceLocation("${Turtlematic.MOD_ID}:turtle_${ID.path}_upgrade_left", "inventory")
        private val RIGHT_MODEL = ModelResourceLocation("${Turtlematic.MOD_ID}:turtle_${ID.path}_upgrade_right", "inventory")
    }

    override val leftModel: ModelResourceLocation
        get() = LEFT_MODEL

    override val rightModel: ModelResourceLocation
        get() = RIGHT_MODEL

    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): CreativeChestPeripheral {
        return CreativeChestPeripheral(TurtlePeripheralOwner(turtle, side))
    }
}