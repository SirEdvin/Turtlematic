package site.siredvin.lib.turtle

import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.peripherals.api.IBasePeripheral
import site.siredvin.turtlematic.util.turtleAdjective

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
        turtleAdjective(id.path),
        item
    )
}