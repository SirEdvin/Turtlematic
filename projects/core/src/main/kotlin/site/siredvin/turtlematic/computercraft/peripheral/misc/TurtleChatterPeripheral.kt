package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class TurtleChatterPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {

    companion object: PeripheralConfiguration {
        override val TYPE = "chatter"
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