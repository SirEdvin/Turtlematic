package site.siredvin.lib.peripherals.ability

import site.siredvin.lib.peripherals.api.IOwnerAbility

class PeripheralOwnerAbility<T: IOwnerAbility> {
    companion object {
        val FUEL: PeripheralOwnerAbility<FuelAbility<*>> = PeripheralOwnerAbility()
        val OPERATION: PeripheralOwnerAbility<OperationAbility> = PeripheralOwnerAbility()
        val EXPERIENCE: PeripheralOwnerAbility<ExperienceAbility> = PeripheralOwnerAbility()
    }
}