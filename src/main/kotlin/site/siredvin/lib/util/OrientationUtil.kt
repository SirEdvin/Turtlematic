package site.siredvin.lib.util

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.core.computer.ComputerSide
import net.minecraft.core.Direction

object OrientationUtil {
    @Throws(LuaException::class)
    fun getDirection(facing: Direction, computerSide: String): Direction {
        if (Direction.byName(computerSide) != null) return Direction.byName(computerSide)!!
        if (computerSide == ComputerSide.FRONT.toString()) return facing
        if (computerSide == ComputerSide.BACK.toString()) return facing.opposite
        if (computerSide == ComputerSide.TOP.toString()) return Direction.UP
        if (computerSide == ComputerSide.BOTTOM.toString()) return Direction.DOWN
        if (computerSide == ComputerSide.RIGHT.toString()) return facing.counterClockWise
        if (computerSide == ComputerSide.LEFT.toString()) return facing.clockWise
        throw LuaException("$computerSide is not a valid side")
    }
}