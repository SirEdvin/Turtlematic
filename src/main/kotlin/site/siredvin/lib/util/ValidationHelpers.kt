package site.siredvin.lib.util

import dan200.computercraft.api.lua.LuaException

fun assertBetween(arg: Int, min: Int, max: Int, name: String) {
    if (arg < min || arg > max)
        throw LuaException("$name should be between $min and $max")
}
