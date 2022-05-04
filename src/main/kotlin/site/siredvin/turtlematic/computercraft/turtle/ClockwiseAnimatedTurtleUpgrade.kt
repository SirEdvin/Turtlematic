package site.siredvin.turtlematic.computercraft.turtle

import site.siredvin.lib.api.peripheral.IBasePeripheral
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.api.client.TransformedModel
import com.mojang.blaze3d.vertex.PoseStack
import site.siredvin.turtlematic.util.DataStorageUtil
import com.mojang.math.Vector3f
import com.mojang.math.Transformation
import site.siredvin.lib.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore

abstract class ClockwiseAnimatedTurtleUpgrade<T : IBasePeripheral<*>> : PeripheralTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    companion object {
        fun <T : IBasePeripheral<*>> dynamic(item: BaseAutomataCore, constructor: AutomataPeripheralBuildFunction<T>): ClockwiseAnimatedTurtleUpgrade<T> {
            return Dynamic(item.turtleID, item, constructor)
        }
    }

    private class Dynamic<T : IBasePeripheral<*>>(
        id: ResourceLocation, private val item: BaseAutomataCore, private val constructor: AutomataPeripheralBuildFunction<T>
        ): ClockwiseAnimatedTurtleUpgrade<T>(id, item.defaultInstance) {
        override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): T {
            return constructor.build(turtle, side, item.coreTier)
        }

    }

    override fun getModel(turtleAccess: ITurtleAccess?, turtleSide: TurtleSide): TransformedModel {
        if (leftModel == null) {
            val stack = PoseStack()
            stack.pushPose()
            stack.translate(0.0, 0.5, 0.5)
            if (turtleAccess != null) {
                val rotationStep = DataStorageUtil.RotationCharge[turtleAccess, turtleSide]
                stack.mulPose(Vector3f.XN.rotationDegrees((-10 * rotationStep).toFloat()))
            }
            stack.translate(0.0, -0.5, -0.5)
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

    // Optional callbacks for addons
    fun chargeConsumingCallback() {}

    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        super.update(turtle, side)
        if (DataStorageUtil.RotationCharge.consume(turtle, side))
            chargeConsumingCallback()
    }
}