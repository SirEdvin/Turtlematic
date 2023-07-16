package site.siredvin.turtlematic.client

import com.mojang.blaze3d.vertex.PoseStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape

object MimicTurtleRenderTrick : TurtleRenderTrick {
    override val cancelTurtleRender: Boolean
        get() = true

    override fun render(
        turtle: TurtleBlockEntity,
        access: ITurtleAccess,
        side: TurtleSide,
        partialTicks: Float,
        transform: PoseStack,
        buffers: MultiBufferSource,
        lightmapCoord: Int,
        overlayLight: Int,
    ) {
        val state = Blocks.CAULDRON.defaultBlockState()
        val minecraft = Minecraft.getInstance()
        when (state.renderShape) {
            RenderShape.MODEL -> minecraft.blockRenderer.modelRenderer.renderModel(
                transform.last(), buffers.getBuffer(RenderType.translucent()), state,
                minecraft.blockRenderer.getBlockModel(state), 1f, 1f, 1f, lightmapCoord, overlayLight,
            )
            RenderShape.ENTITYBLOCK_ANIMATED -> {
                val block = state.block as? BaseEntityBlock ?: return
                val blockEntity = block.newBlockEntity(turtle.blockPos, state) ?: return
                minecraft.blockEntityRenderDispatcher.render(blockEntity, partialTicks, transform, buffers)
            }
            else -> {}
        }
    }
}
