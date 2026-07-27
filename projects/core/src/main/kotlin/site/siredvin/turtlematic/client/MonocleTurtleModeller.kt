package site.siredvin.turtlematic.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.math.Transformation
import dan200.computercraft.api.client.TransformedModel
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.component.DataComponentPatch

class MonocleTurtleModeller<T : ITurtleUpgrade> : TurtleUpgradeModeller<T> {

    override fun getModel(upgrade: T, turtle: ITurtleAccess?, side: TurtleSide, data: DataComponentPatch): TransformedModel {
        val stack = PoseStack()
        stack.pushPose()
        if (side == TurtleSide.LEFT) {
            stack.scale(0.7f, 0.7f, 0.7f)
            stack.mulPose(Axis.YN.rotationDegrees(180f))
            stack.translate(-0.85, 0.17, -0.65)
        } else {
            stack.scale(0.7f, 0.7f, 0.7f)
            stack.translate(0.55, 0.17, -0.35)
        }
        return TransformedModel.of(upgrade.craftingItem, Transformation(stack.last().pose()))
    }
}
