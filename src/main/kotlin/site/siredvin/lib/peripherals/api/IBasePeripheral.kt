package site.siredvin.lib.peripherals.api

import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IPeripheral

interface IBasePeripheral<T : IPeripheralOwner?> : IPeripheral {
    val isEnabled: Boolean
    val connectedComputers: List<IComputerAccess>
    val peripheralOwner: T
}