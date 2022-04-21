package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.LuaException
import java.util.*
import java.util.stream.Collectors

enum class InteractionMode {
    BOTH, ENTITY, BLOCK;

    val skipEntry: Boolean
        get() = this == BLOCK

    val skipBlock: Boolean
        get() = this == ENTITY

    companion object {
        fun luaValueOf(name: String): InteractionMode {
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

