package site.siredvin.lib.blocks

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty

class LibTileEntityBlock<T : BlockEntity?> : BaseTileEntityBlock {
    private val tileEntity: BlockEntityType<T>
    private val isRotatable: Boolean

    constructor(tileEntity: BlockEntityType<T>, isRotatable: Boolean) : super(false) {
        this.tileEntity = tileEntity
        this.isRotatable = isRotatable
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.SOUTH))
    }

    constructor(tileEntity: BlockEntityType<T>, isRotatable: Boolean, belongToTickingEntity: Boolean) : super(
        belongToTickingEntity
    ) {
        this.tileEntity = tileEntity
        this.isRotatable = isRotatable
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.SOUTH))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return tileEntity.create(pos, state)
    }

    override fun rotate(state: BlockState, rot: Rotation): BlockState {
        return if (isRotatable) state.setValue(
            FACING,
            rot.rotate(state.getValue(FACING))
        ) else state
    }

    override fun mirror(state: BlockState, mirrorIn: Mirror): BlockState {
        return if (isRotatable) state.rotate(mirrorIn.getRotation(state.getValue(FACING))) else state
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return if (isRotatable) defaultBlockState().setValue(
            FACING,
            context.nearestLookingDirection.opposite.opposite
        ) else defaultBlockState()
    }

    companion object {
        val FACING: DirectionProperty = DirectionalBlock.FACING
    }
}