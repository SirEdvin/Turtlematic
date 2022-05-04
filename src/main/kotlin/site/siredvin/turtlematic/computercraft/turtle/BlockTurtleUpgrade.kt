package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.client.resources.model.ModelResourceLocation
import site.siredvin.lib.api.TurtlePeripheralBuildFunction
import site.siredvin.lib.api.peripheral.IBasePeripheral
import site.siredvin.lib.common.items.TurtleItem
import site.siredvin.lib.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.TurtlematicClient

abstract class BlockTurtleUpgrade<T : IBasePeripheral<*>>(item: TurtleItem): PeripheralTurtleUpgrade<T>(item.turtleID, item.defaultInstance) {

    companion object {
        fun <T : IBasePeripheral<*>> dynamic(item: TurtleItem, constructor: TurtlePeripheralBuildFunction<T>): BlockTurtleUpgrade<T> {
            return Dynamic(item, constructor)
        }
    }

    private class Dynamic<T : IBasePeripheral<*>>(private val item: TurtleItem, private val constructor: TurtlePeripheralBuildFunction<T>): BlockTurtleUpgrade<T>(item) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side)
        }

    }

    override val leftModel: ModelResourceLocation
        get() = TurtlematicClient.getLeftTurtleUpgradeModel(upgradeID)

    override val rightModel: ModelResourceLocation
        get() = TurtlematicClient.getRightTurtleUpgradeModel(upgradeID)
}