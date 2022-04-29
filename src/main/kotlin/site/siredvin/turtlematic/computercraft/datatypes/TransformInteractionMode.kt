package site.siredvin.turtlematic.computercraft.datatypes

import dan200.computercraft.api.lua.LuaException
import java.util.stream.Collectors

enum class TransformInteractionMode {
    INVENTORY, BLOCK;

    companion object {
        fun luaValueOf(name: String): TransformInteractionMode {
            return luaValueOf(name, setOf(*values()))
        }
        fun luaValueOf(name: String, allowedMods: Set<TransformInteractionMode>): TransformInteractionMode {
            try {
                return TransformInteractionMode.valueOf(name.uppercase())
            } catch (exc: IllegalArgumentException) {
                val allValues = allowedMods.stream().map { mode -> mode.name.lowercase() }.collect(
                    Collectors.toList()
                ).joinToString(", ")
                throw LuaException("Transform interaction mode should be one of: $allValues")
            }
        }
    }
}