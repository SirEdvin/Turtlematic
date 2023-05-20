package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class LavaBucketPeripheral(turtle: ITurtleAccess, side: TurtleSide):
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {
    companion object: PeripheralConfiguration {
        override val TYPE = "lava_bucket"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableLavaBucket

    @LuaFunction(mainThread = true)
    fun void() {
        peripheralOwner.turtle.inventory.setItem(peripheralOwner.turtle.selectedSlot, ItemStack.EMPTY)
    }
}