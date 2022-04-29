package site.siredvin.lib.computercraft.peripheral

import site.siredvin.lib.computercraft.peripheral.owner.PocketPeripheralOwner

object DisabledPeripheral: BasePeripheral<PocketPeripheralOwner?>("disabled", PocketPeripheralOwner(null)) {
    override val isEnabled: Boolean
        get() = true
}