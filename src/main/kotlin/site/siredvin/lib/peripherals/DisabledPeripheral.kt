package site.siredvin.lib.peripherals

import site.siredvin.lib.peripherals.owner.PocketPeripheralOwner

object DisabledPeripheral: BasePeripheral<PocketPeripheralOwner?>("disabled", PocketPeripheralOwner(null)) {
    override val isEnabled: Boolean
        get() = true
}