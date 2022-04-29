package site.siredvin.lib.turtle

import site.siredvin.lib.peripherals.api.IBasePeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dan200.computercraft.api.turtle.TurtleUpgradeType
import site.siredvin.turtlematic.util.turtleAdjective

abstract class PeripheralToolTurtleUpgrade<T : IBasePeripheral<*>> : BaseTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(
        id,
        TurtleUpgradeType.BOTH,
        adjective,
        item
    )

    constructor(id: ResourceLocation, item: ItemStack) : super(
        id,
        TurtleUpgradeType.BOTH,
        turtleAdjective(id.path),
        item
    )
}