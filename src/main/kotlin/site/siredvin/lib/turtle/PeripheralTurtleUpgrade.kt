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

abstract class PeripheralTurtleUpgrade<T : IBasePeripheral<*>> : AbstractTurtleUpgrade {
    protected var tick = 0

    constructor(id: ResourceLocation, adjective: String?, item: ItemStack?) : super(
        id,
        TurtleUpgradeType.PERIPHERAL,
        adjective,
        item
    ) {
    }

    constructor(id: ResourceLocation, item: ItemStack?) : super(
        id,
        TurtleUpgradeType.PERIPHERAL,
        turtleAdjective(id.path),
        item
    ) {
    }

    protected val leftModel: ModelResourceLocation?
        get() = null

    protected val rightModel: ModelResourceLocation?
        get() = null

    protected abstract fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T

    override fun getModel(turtleAccess: ITurtleAccess?, turtleSide: TurtleSide): TransformedModel {
        if (leftModel == null) {
            val xOffset = if (turtleSide == TurtleSide.LEFT) -0.40625f else 0.40625f
            val transform = Matrix4f()
            transform.load(
                FloatBuffer.wrap(
                    floatArrayOf(
                        0.0f, 0.0f, -1.0f, 1.0f + xOffset,
                        1.0f, 0.0f, 0.0f, 0.0f,
                        0.0f, -1.0f, 0.0f, 1.0f,
                        0.0f, 0.0f, 0.0f, 1.0f
                    )
                )
            )
            return TransformedModel.of(craftingItem, Transformation(transform))
        }
        return TransformedModel.of(if (turtleSide == TurtleSide.LEFT) leftModel!! else rightModel!!)
    }

    override fun createPeripheral(turtle: ITurtleAccess, side: TurtleSide): IPeripheral? {
        val peripheral = buildPeripheral(turtle, side)
        return if (!peripheral.isEnabled) {
            DisabledPeripheral.INSTANCE
        } else peripheral
    }
}