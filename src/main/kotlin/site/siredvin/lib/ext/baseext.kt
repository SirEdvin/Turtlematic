package site.siredvin.lib.ext

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

fun BlockPos.toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
