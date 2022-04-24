package site.siredvin.turtlematic.integrations.computercraft.plugins

import dan200.computercraft.api.lua.LuaException
import java.util.stream.Collectors

enum class InteractionMode {
    ANY, ENTITY, BLOCK;

    val skipEntry: Boolean
        get() = this == BLOCK

    val skipBlock: Boolean
        get() = this == ENTITY

    companion object {
        fun optValueOf(name: String): InteractionMode? {
            return try {
                valueOf(name.uppercase())
            } catch (exc: IllegalArgumentException) {
                null
            }
        }

        fun luaValueOf(name: String, allowedMods: Set<InteractionMode>): InteractionMode {
            if (name == "*")
                return ANY
            try {
                return valueOf(name.uppercase())
            } catch (exc: IllegalArgumentException) {
                val allValues = allowedMods.stream().map { mode -> mode.name.lowercase() }.collect(
                    Collectors.toList()
                ).joinToString(", ")
                throw LuaException("Interaction mode should be one of: $allValues")
            }
        }
    }
}

