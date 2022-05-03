package site.siredvin.lib.util.world

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.piston.MovingPistonBlock
import net.minecraft.world.level.block.piston.PistonStructureResolver
import net.minecraft.world.level.block.state.BlockState

object PistonSimulation {
    fun move(level: Level, resolver: PistonStructureResolver, direction: Direction, isExtending: Boolean = true) {
        val cleanupRequiredTracker: MutableMap<BlockPos, BlockState> = hashMapOf()
        val blockStateCache: MutableMap<BlockPos, BlockState> = hashMapOf()
        val blocksToPush = resolver.toPush
        blocksToPush.forEach {
            val blockState = level.getBlockState(it)
            cleanupRequiredTracker[it] = blockState
        }
        val blocksToDestroy = resolver.toDestroy
        val movingDirection = if (isExtending) direction else direction.opposite

        blocksToDestroy.asReversed().forEach {
            val blockState = level.getBlockState(it)
            val blockEntity = if(blockState.hasBlockEntity()) level.getBlockEntity(it) else null
            Block.dropResources(blockState, level, it, blockEntity)
            level.setBlock(it, Blocks.AIR.defaultBlockState(), 18)
            if (!blockState.`is`(BlockTags.FIRE)) {
                level.addDestroyBlockEffect(it, blockState)
            }
            blockStateCache[it] = blockState
        }

        blocksToPush.asReversed().forEach {
            val blockState = level.getBlockState(it)
            val targetPos = it.relative(movingDirection)
            cleanupRequiredTracker.remove(targetPos)
            val newBlockState = Blocks.MOVING_PISTON.defaultBlockState().setValue(DirectionalBlock.FACING, direction)
            level.setBlock(targetPos, newBlockState, 68)
            level.setBlockEntity(
                MovingPistonBlock.newMovingBlockEntity(
                targetPos,
                newBlockState,
                blockState,
                direction,
                isExtending,
                false
            ))
            blockStateCache[it] = blockState
        }

        val airBlock = Blocks.AIR.defaultBlockState()
        cleanupRequiredTracker.entries.forEach {
            level.setBlock(it.key, airBlock, 82)
            it.value.updateIndirectNeighbourShapes(level, it.key, 2)
            airBlock.updateNeighbourShapes(level, it.key, 2)
            airBlock.updateIndirectNeighbourShapes(level, it.key, 2)
        }

        blocksToDestroy.asReversed().forEach {
            val blockState = blockStateCache[it]!!
            blockState.updateIndirectNeighbourShapes(level, it, 2)
            level.updateNeighborsAt(it,blockState.block)
        }

        blocksToPush.asReversed().forEach {
            level.updateNeighborsAt(it, blockStateCache[it]!!.block)
        }
    }
}