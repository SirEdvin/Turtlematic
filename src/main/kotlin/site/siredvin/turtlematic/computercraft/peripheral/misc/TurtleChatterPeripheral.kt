package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import site.siredvin.peripheralium.computercraft.peripheral.BasePeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class TurtleChatterPeripheral(peripheralOwner: TurtlePeripheralOwner) :
    BasePeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {

    companion object {
        const val TYPE = "chatter"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableTurtleChatter

    @LuaFunction(mainThread = true)
    fun getMessage(): String? {
       return DataStorageObjects.TurtleChat.getMessage(peripheralOwner)
    }

    @LuaFunction(mainThread = true)
    fun setMessage(text: String) {
        DataStorageObjects.TurtleChat.setMessage(peripheralOwner, text)
    }

    @LuaFunction(mainThread = true)
    fun clearMessage() {
        DataStorageObjects.TurtleChat.setMessage(peripheralOwner, null)
    }
}