package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.PeripheralConfiguration

class MimicPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {
    companion object : PeripheralConfiguration {
        override val TYPE = "mimic"
    }

    override val isEnabled: Boolean
        // TODO: fix this
        get() = true
}
