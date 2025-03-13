package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.world.item.ItemStack
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner

class LavaBucketPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {
    companion object : PeripheralConfiguration {
        override val type = "lava_bucket"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableLavaBucket

    @LuaFunction(mainThread = true)
    fun void() {
        peripheralOwner.turtle.inventory.setItem(peripheralOwner.turtle.selectedSlot, ItemStack.EMPTY)
    }
}
