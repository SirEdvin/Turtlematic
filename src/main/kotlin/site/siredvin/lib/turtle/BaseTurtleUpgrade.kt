package site.siredvin.lib.turtle

import com.mojang.blaze3d.vertex.PoseStack
import site.siredvin.lib.peripherals.api.IBasePeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeType
import net.minecraft.client.resources.model.ModelResourceLocation
import com.mojang.math.Transformation
import com.mojang.math.Vector3f
import dan200.computercraft.api.peripheral.IPeripheral
import site.siredvin.lib.peripherals.DisabledPeripheral

abstract class BaseTurtleUpgrade<T : IBasePeripheral<*>>(
    id: ResourceLocation,
    type: TurtleUpgradeType,
    adjective: String,
    stack: ItemStack
) : AbstractTurtleUpgrade(id, type, adjective, stack) {

    protected open val leftModel: ModelResourceLocation?
        get() = null

    protected open val rightModel: ModelResourceLocation?
        get() = null

    protected abstract fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T

    override fun getModel(turtleAccess: ITurtleAccess?, turtleSide: TurtleSide): TransformedModel {
        if (leftModel == null) {
            val stack = PoseStack()
            stack.pushPose()
            stack.mulPose(Vector3f.YN.rotationDegrees(90f))
            if (turtleSide == TurtleSide.LEFT) {
                stack.translate(0.0, 0.0, -0.6)
            } else {
                stack.translate(0.0, 0.0, -1.4)
            }
            return TransformedModel.of(craftingItem, Transformation(stack.last().pose()))
        }
        return TransformedModel.of(if (turtleSide == TurtleSide.LEFT) leftModel!! else rightModel!!)
    }

    override fun createPeripheral(turtle: ITurtleAccess, side: TurtleSide): IPeripheral? {
        val peripheral = buildPeripheral(turtle, side)
        return if (!peripheral.isEnabled) {
            DisabledPeripheral
        } else peripheral
    }
}