package site.siredvin.lib.util

import dan200.computercraft.api.lua.LuaException
import net.minecraft.core.BlockPos
import kotlin.math.abs

fun assertBetween(arg: Int, min: Int, max: Int, name: String) {
    if (arg < min || arg > max)
        throw LuaException("$name should be between $min and $max")
}

fun radiusCorrect(first: BlockPos, second: BlockPos, radius: Int): Boolean {
    if (abs(first.x - second.x) > radius) return false
    return if (abs(first.y - second.y) > radius) false else abs(first.z - second.z) <= radius
}

@Throws(LuaException::class)
fun isCorrectSlot(slot: Int) {
    isCorrectSlot(slot, "target")
}

@Throws(LuaException::class)
fun isCorrectSlot(slot: Int, name: String?) {
    if (slot < 1 || slot > 16) throw LuaException(String.format("%s slot is incorrectly defined", name))
}
