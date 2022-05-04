package site.siredvin.lib.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
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
            return Dynamic(item, constructor)
        }
    }

    private class Dynamic<T : IBasePeripheral<*>>(item: TurtleItem, private val constructor: TurtlePeripheralBuildFunction<T>): PeripheralTurtleUpgrade<T>(item.turtleID, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side)
        }

    }
}