package site.siredvin.turtlematic.integrations.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import site.siredvin.lib.peripherals.BasePeripheral
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.DataStorageUtil
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
       return DataStorageUtil.TurtleChat.getMessage(peripheralOwner)
    }

    @LuaFunction(mainThread = true)
    fun setMessage(text: String) {
        DataStorageUtil.TurtleChat.setMessage(peripheralOwner, text)
    }

    @LuaFunction(mainThread = true)
    fun clearMessage() {
        DataStorageUtil.TurtleChat.setMessage(peripheralOwner, null)
    }
}