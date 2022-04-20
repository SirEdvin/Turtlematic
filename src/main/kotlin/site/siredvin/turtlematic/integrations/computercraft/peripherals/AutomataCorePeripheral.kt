package site.siredvin.turtlematic.integrations.computercraft.peripherals

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral

class AutomataCorePeripheral: IPeripheral{

    @LuaFunction
    fun hello(): String {
        return "hello"
    }

    override fun equals(other: IPeripheral?): Boolean {
        return other is AutomataCorePeripheral
    }

    override fun getType(): String {
        return "automataCore"
    }

}