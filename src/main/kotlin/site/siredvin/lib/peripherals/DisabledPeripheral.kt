package site.siredvin.lib.peripherals

import dan200.computercraft.api.pocket.IPocketAccess
import site.siredvin.lib.peripherals.owner.PocketPeripheralOwner

class DisabledPeripheral private constructor(type: String, access: IPocketAccess?) :
    BasePeripheral<PocketPeripheralOwner?>(
        type, PocketPeripheralOwner(
            access!!
        )
    ) {
    override val isEnabled: Boolean
        get() = true

    companion object {
        @JvmField
        val INSTANCE = DisabledPeripheral("disabledPeripheral", null)
    }
}