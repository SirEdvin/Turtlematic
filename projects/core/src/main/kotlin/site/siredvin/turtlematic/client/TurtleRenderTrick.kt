package site.siredvin.turtlematic.client

import com.mojang.blaze3d.vertex.PoseStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.nbt.CompoundTag

interface TurtleRenderTrick {
    fun render(turtle: TurtleBlockEntity, access: ITurtleAccess, side: TurtleSide, upgradeData: CompoundTag, partialTicks: Float, transform: PoseStack, buffers: MultiBufferSource, lightmapCoord: Int, overlayLight: Int): RenderTrickOpcode
}
