package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.computercraft.peripheral.BasePeripheral
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class LavaBucketPeripheral(peripheralOwner: TurtlePeripheralOwner):
    BasePeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {
    companion object {
        const val TYPE = "lava_bucket"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableLavaBucket

    @LuaFunction(mainThread = true)
    fun void() {
        peripheralOwner.turtle.inventory.setItem(peripheralOwner.turtle.selectedSlot, ItemStack.EMPTY)
    }
}