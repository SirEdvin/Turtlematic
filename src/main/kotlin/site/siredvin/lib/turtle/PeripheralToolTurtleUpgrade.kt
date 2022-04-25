package site.siredvin.lib.turtle

import site.siredvin.lib.peripherals.IBasePeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.client.resources.model.ModelResourceLocation
import com.mojang.math.Matrix4f
import com.mojang.math.Transformation
import java.nio.FloatBuffer
import dan200.computercraft.api.peripheral.IPeripheral
import site.siredvin.lib.peripherals.DisabledPeripheral
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