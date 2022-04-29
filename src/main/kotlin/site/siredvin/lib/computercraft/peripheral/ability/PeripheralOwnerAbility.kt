package site.siredvin.lib.computercraft.peripheral.ability

import site.siredvin.lib.api.peripheral.IOwnerAbility

class PeripheralOwnerAbility<T: IOwnerAbility> {
    companion object {
        val FUEL: PeripheralOwnerAbility<FuelAbility<*>> = PeripheralOwnerAbility()
        val OPERATION: PeripheralOwnerAbility<OperationAbility> = PeripheralOwnerAbility()
        val EXPERIENCE: PeripheralOwnerAbility<ExperienceAbility> = PeripheralOwnerAbility()
    }
}