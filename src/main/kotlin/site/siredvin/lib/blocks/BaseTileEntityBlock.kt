package site.siredvin.lib.blocks

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import site.siredvin.lib.peripherals.IPeripheralTileEntity

abstract class BaseTileEntityBlock(
    private val belongToTickingEntity: Boolean,
    properties: Properties = Properties.of(Material.METAL).strength(1f, 5f).sound(SoundType.METAL).noOcclusion()
): BaseEntityBlock(properties) {

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        if (level.isClientSide || !belongToTickingEntity)
            return null
        return BlockEntityTicker { _, _, _, entity ->
            if (entity is IPeripheralTileEntity) {
                entity.handleTick(level, state, type)
            }
        }
    }
}