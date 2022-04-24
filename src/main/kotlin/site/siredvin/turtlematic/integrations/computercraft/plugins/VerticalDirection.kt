package site.siredvin.turtlematic.integrations.computercraft.plugins

import dan200.computercraft.api.lua.LuaException
import net.minecraft.core.Direction
import java.util.*
import java.util.stream.Collectors

enum class VerticalDirection(val minecraftDirection: Direction) {
    UP(Direction.UP), DOWN(Direction.DOWN);

    companion object {
        fun luaValueOf(name: String): VerticalDirection {
            try {
                return valueOf(name.uppercase())
            } catch (exc: IllegalArgumentException) {
                val allValues = Arrays.stream(values()).map { mode -> mode.name.lowercase() }.collect(
                    Collectors.toList()
                ).joinToString(", ")
                throw LuaException("Interaction mode should be one of: $allValues")
            }
        }
    }
}