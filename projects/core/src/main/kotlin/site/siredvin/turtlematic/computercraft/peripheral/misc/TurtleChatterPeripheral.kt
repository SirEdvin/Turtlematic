package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.util.DataStorageObjects

class TurtleChatterPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val TYPE = "chatter"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableTurtleChatter

    @LuaFunction(mainThread = true)
    fun getMessage(): String? {
        return DataStorageObjects.TurtleChat[peripheralOwner]
    }

    @LuaFunction(mainThread = true)
    fun setMessage(text: String) {
        DataStorageObjects.TurtleChat[peripheralOwner] = text
    }

    @LuaFunction(mainThread = true)
    fun clearMessage() {
        DataStorageObjects.TurtleChat[peripheralOwner] = null
    }
}
