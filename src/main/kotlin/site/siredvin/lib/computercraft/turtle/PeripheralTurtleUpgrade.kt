package site.siredvin.lib.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.api.TurtleIDBuildFunction
import site.siredvin.lib.api.TurtlePeripheralBuildFunction
import site.siredvin.lib.api.peripheral.IBasePeripheral
import site.siredvin.lib.common.items.TurtleItem
import site.siredvin.lib.util.turtleAdjective

abstract class PeripheralTurtleUpgrade<T : IBasePeripheral<*>> : BaseTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(
        id,
        TurtleUpgradeType.PERIPHERAL,
        adjective,
        item
    )

    constructor(id: ResourceLocation, item: ItemStack) : super(
        id,
        TurtleUpgradeType.PERIPHERAL,
        turtleAdjective(id),
        item
    )

    companion object {
        fun <T : IBasePeripheral<*>> dynamic(item: TurtleItem, constructor: TurtlePeripheralBuildFunction<T>): PeripheralTurtleUpgrade<T> {
            return Dynamic(item.turtleID, item.defaultInstance, constructor)
        }

        fun <T : IBasePeripheral<*>> dynamic(item: Item, constructor: TurtlePeripheralBuildFunction<T>, idBuilder: TurtleIDBuildFunction): PeripheralTurtleUpgrade<T> {
            return Dynamic(idBuilder.get(item), item.defaultInstance, constructor)
        }
    }

    private class Dynamic<T : IBasePeripheral<*>>(
        turtleID: ResourceLocation, stack: ItemStack, private val constructor: TurtlePeripheralBuildFunction<T>
        ): PeripheralTurtleUpgrade<T>(turtleID, stack) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side)
        }

    }
}