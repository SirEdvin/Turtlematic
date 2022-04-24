package site.siredvin.lib.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.phys.Vec3

fun BlockPos.toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
fun BlockPos.toRelative(facing: Direction): BlockPos {
    return when (facing) {
        Direction.UP, Direction.DOWN -> throw IllegalArgumentException("This works only for horizontal facing")
        Direction.NORTH -> this.rotate(Rotation.CLOCKWISE_90)
        Direction.SOUTH -> this.rotate(Rotation.COUNTERCLOCKWISE_90)
        Direction.EAST -> this
        Direction.WEST -> this.rotate(Rotation.CLOCKWISE_180)
    }
}
