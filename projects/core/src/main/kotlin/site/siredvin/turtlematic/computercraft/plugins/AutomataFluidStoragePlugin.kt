package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral

class AutomataFluidStoragePlugin(private val tankCount: Int, private val tankCapacity: Int, automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {

    @LuaFunction(mainThread = true)
    fun storeFluid(): MethodResult {
        return MethodResult.of()
    }

    @LuaFunction(mainThread = true)
    fun placeFluid(destination: String, name: String): MethodResult {
        return MethodResult.of()
    }

    @LuaFunction(mainThread = true)
    fun getStored(): MethodResult {
        return MethodResult.of()
    }
}
