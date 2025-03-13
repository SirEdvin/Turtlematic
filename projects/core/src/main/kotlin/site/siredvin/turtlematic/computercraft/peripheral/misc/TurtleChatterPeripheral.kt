package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner

class TurtleChatterPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val type = "chatter"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableTurtleChatter

    @LuaFunction(mainThread = true)
    fun getMessage(): String? = DataStorageObjects.TurtleChat[peripheralOwner]

    @LuaFunction(mainThread = true)
    fun setMessage(text: String) {
        DataStorageObjects.TurtleChat[peripheralOwner] = text
    }

    @LuaFunction(mainThread = true)
    fun clearMessage() {
        DataStorageObjects.TurtleChat[peripheralOwner] = null
    }
}
