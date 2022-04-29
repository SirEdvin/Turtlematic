package site.siredvin.turtlematic.computercraft.datatypes

import dan200.computercraft.api.lua.LuaException
import java.util.stream.Collectors

enum class AreaInteractionMode {
    ENTITY, BLOCK, ITEM;

    companion object {
        fun luaValueOf(name: String, allowedMods: Set<AreaInteractionMode>): AreaInteractionMode {
            try {
                return valueOf(name.uppercase())
            } catch (exc: IllegalArgumentException) {
                val allValues = allowedMods.stream().map { mode -> mode.name.lowercase() }.collect(
                    Collectors.toList()
                ).joinToString(", ")
                throw LuaException("Area interaction mode should be one of: $allValues")
            }
        }
    }
}

