package site.siredvin.turtlematic.client

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.mojang.blaze3d.vertex.PoseStack
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.turtlematic.util.DataStorageObjects
import java.util.concurrent.TimeUnit

object MimicTurtleRenderTrick : TurtleRenderTrick {

    private val fakeBlockEntityCache = CacheBuilder.newBuilder()
        .expireAfterAccess(30, TimeUnit.SECONDS).maximumSize(10_000)
        .build(CacheLoader.from(::getBlockEntity))

    private val dummyBlockEntity = object : BlockEntity(BlockEntityType.BARREL, BlockPos(0, 0, 0), Blocks.BARREL.defaultBlockState()) {
    }

    private val emptyCompoundTag = CompoundTag()

    fun getBlockEntity(data: Triple<BlockState, BlockPos, CompoundTag>): BlockEntity {
        val block = data.first.block as? EntityBlock ?: return dummyBlockEntity
        val blockEntity = block.newBlockEntity(data.second, data.first) ?: dummyBlockEntity
        blockEntity.load(data.third)
        return blockEntity
    }

    fun renderBlockModel(minecraft: Minecraft, transform: PoseStack, buffers: MultiBufferSource, state: BlockState, lightmapCoord: Int, overlayLight: Int) {
        @Suppress("DEPRECATION", "KotlinRedundantDiagnosticSuppress")
        minecraft.blockRenderer.modelRenderer.renderModel(
            transform.last(), buffers.getBuffer(RenderType.translucent()), state,
            minecraft.blockRenderer.getBlockModel(state), 1f, 1f, 1f, lightmapCoord, overlayLight,
        )
    }

    fun renderBlockEntity(minecraft: Minecraft, state: BlockState, turtle: TurtleBlockEntity, upgradeData: CompoundTag, partialTicks: Float, transform: PoseStack, buffers: MultiBufferSource) {
        if (state.block is EntityBlock) {
            val entity = fakeBlockEntityCache.get(
                Triple(
                    state,
                    turtle.blockPos,
                    DataStorageObjects.MimicExtraData[upgradeData] ?: emptyCompoundTag,
                ),
            )
            // Yep, this is a check for SAME OBJECT
            if (entity !== dummyBlockEntity) {
                entity.level = turtle.level
                minecraft.blockEntityRenderDispatcher.render(entity, partialTicks, transform, buffers)
                // entity.level = null; Setting this to null causes a crash when rendering a beacon.
            }
        }
    }

    override fun render(
        turtle: TurtleBlockEntity,
        access: ITurtleAccess,
        side: TurtleSide,
        upgradeData: CompoundTag,
        partialTicks: Float,
        transform: PoseStack,
        buffers: MultiBufferSource,
        lightmapCoord: Int,
        overlayLight: Int,
    ): RenderTrickOpcode {
        val state = DataStorageObjects.Mimic[upgradeData] ?: return RenderTrickOpcode.NOOP
        transform.pushPose()
        val minecraft = Minecraft.getInstance()
        val rmlInstructions = DataStorageObjects.RMLInstructions[upgradeData]
        if (rmlInstructions != null) {
            RenderUtil.parseRML(rmlInstructions).forEach {
                it.process(transform)
            }
        }
        when (state.renderShape) {
            RenderShape.MODEL -> {
                renderBlockModel(minecraft, transform, buffers, state, lightmapCoord, overlayLight)
                renderBlockEntity(minecraft, state, turtle, upgradeData, partialTicks, transform, buffers)
            }
            RenderShape.ENTITYBLOCK_ANIMATED -> renderBlockEntity(minecraft, state, turtle, upgradeData, partialTicks, transform, buffers)
            else -> {}
        }
        transform.popPose()
        return RenderTrickOpcode.CANCEL_RENDER
    }
}
