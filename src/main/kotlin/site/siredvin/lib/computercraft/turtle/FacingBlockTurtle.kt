package site.siredvin.lib.computercraft.turtle

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Transformation
import com.mojang.math.Vector3f
import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.api.peripheral.IBasePeripheral

abstract class FacingBlockTurtle<T : IBasePeripheral<*>>: PeripheralTurtleUpgrade<T> {
    constructor(id: ResourceLocation, adjective: String, item: ItemStack) : super(id, adjective, item)
    constructor(id: ResourceLocation, item: ItemStack) : super(id, item)

    override fun getModel(turtleAccess: ITurtleAccess?, turtleSide: TurtleSide): TransformedModel {
        if (leftModel == null) {
            val stack = PoseStack()
            stack.pushPose()
            stack.scale(0.3f, 0.3f, 0.3f)
            stack.mulPose(Vector3f.XN.rotationDegrees(90f))
            stack.translate(0.0, -2.0, 1.05)
            if (turtleSide == TurtleSide.LEFT) {
                stack.translate(-0.6, 0.0, 0.0)
            } else {
                stack.translate(2.9, 0.0, 0.0)
            }
            return TransformedModel.of(craftingItem, Transformation(stack.last().pose()))
        }
        return TransformedModel.of(if (turtleSide == TurtleSide.LEFT) leftModel!! else rightModel!!)
    }
}