package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.client.resources.model.ModelResourceLocation
import site.siredvin.peripheralium.api.TurtlePeripheralBuildFunction
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import site.siredvin.peripheralium.common.items.TurtleItem
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.TurtlematicClient

abstract class BlockTurtleUpgrade<T : IOwnedPeripheral<*>>(item: TurtleItem): PeripheralTurtleUpgrade<T>(item.turtleID, item.defaultInstance) {

    companion object {
        fun <T : IOwnedPeripheral<*>> dynamic(item: TurtleItem, constructor: TurtlePeripheralBuildFunction<T>): BlockTurtleUpgrade<T> {
            return Dynamic(item, constructor)
        }
    }

    private class Dynamic<T : IOwnedPeripheral<*>>(private val item: TurtleItem, private val constructor: TurtlePeripheralBuildFunction<T>): BlockTurtleUpgrade<T>(item) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side)
        }

    }

    override val leftModel: ModelResourceLocation
        get() = TurtlematicClient.getLeftTurtleUpgradeModel(upgradeID)

    override val rightModel: ModelResourceLocation
        get() = TurtlematicClient.getRightTurtleUpgradeModel(upgradeID)
}