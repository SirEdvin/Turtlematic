package site.siredvin.turtlematic.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.math.Transformation
import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.util.DataStorageObjects

class ClockwiseTurtleModeller<T: ITurtleUpgrade>: TurtleUpgradeModeller<T> {
    override fun getModel(upgrade: T, turtle: ITurtleAccess?, side: TurtleSide): TransformedModel {
        val stack = PoseStack()
        stack.pushPose()
        stack.translate(0.0, 0.5, 0.5)
        if (turtle != null) {
            val rotationStep = DataStorageObjects.RotationCharge[turtle, side]
            stack.mulPose(Axis.XN.rotationDegrees((-10 * rotationStep).toFloat()))
        }
        stack.translate(0.0, -0.5, -0.5)
        stack.mulPose(Axis.YN.rotationDegrees(90f))
        if (side == TurtleSide.LEFT) {
            stack.translate(0.0, 0.0, -0.6)
        } else {
            stack.translate(0.0, 0.0, -1.4)
        }
        return TransformedModel.of(upgrade.craftingItem, Transformation(stack.last().pose()))
    }
}