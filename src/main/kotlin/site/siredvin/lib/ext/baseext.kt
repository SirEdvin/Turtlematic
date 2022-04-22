package site.siredvin.lib.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec3

fun BlockPos.toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
fun BlockPos.toRelative(facing: Direction): BlockPos {
    return when (facing) {
        Direction.UP, Direction.DOWN -> throw IllegalArgumentException("This works only for horizontal facing")
        Direction.NORTH -> BlockPos(z, y, x)
        Direction.SOUTH -> BlockPos(-z, y, x)
        Direction.EAST -> this
        Direction.WEST -> BlockPos(-x, y, z)
    }
}
