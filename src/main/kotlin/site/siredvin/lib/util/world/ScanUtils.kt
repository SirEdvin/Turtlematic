package site.siredvin.lib.util.world

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.function.BiConsumer

object ScanUtils {

    fun traverseBlocks(
        world: Level,
        center: BlockPos,
        radius: Int,
        consumer: BiConsumer<BlockState, BlockPos>,
        relativePosition: Boolean = false
    ) {
        val x = center.x
        val y = center.y
        val z = center.z
        for (oX in x - radius..x + radius) {
            for (oY in y - radius..y + radius) {
                for (oZ in z - radius..z + radius) {
                    val subPos = BlockPos(oX, oY, oZ)
                    val blockState = world.getBlockState(subPos)
                    if (!blockState.isAir) {
                        if (relativePosition) {
                            consumer.accept(blockState, BlockPos(oX - x, oY - y, oZ - z))
                        } else {
                            consumer.accept(blockState, subPos)
                        }
                    }
                }
            }
        }
    }
}