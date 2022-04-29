package site.siredvin.lib.util.representation

import dan200.computercraft.api.lua.LuaException
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Rotation
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import java.util.stream.Collectors

object LuaInterpretation {
    // BlockPos tricks
    @Throws(LuaException::class)
    fun asBlockPos(table: Map<*, *>): BlockPos {
        if (!table.containsKey("x") || !table.containsKey("y") || !table.containsKey("z")) throw LuaException("Table should be block position table")
        val x = table["x"]
        val y = table["y"]
        val z = table["z"]
        if (x !is Number || y !is Number || z !is Number) throw LuaException("Table should be block position table")
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }

    @Throws(LuaException::class)
    fun asBlockPos(center: BlockPos, table: Map<*, *>): BlockPos {
        val relative = asBlockPos(table)
        return BlockPos(center.x + relative.x, center.y + relative.y, center.z + relative.z)
    }

    @Throws(LuaException::class)
    fun asRotation(rotation: String): Rotation {
        try {
            return Rotation.valueOf(rotation.uppercase())
        } catch (exc: IllegalArgumentException) {
            val allValues = Rotation.values().joinToString(", ") { it.name.lowercase() }
            throw LuaException("Rotation should be one of: $allValues")
        }
    }
}