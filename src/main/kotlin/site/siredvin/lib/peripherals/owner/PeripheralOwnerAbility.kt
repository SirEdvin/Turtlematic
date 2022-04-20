package site.siredvin.lib.peripherals.owner

class PeripheralOwnerAbility<T> {
    companion object {
        val FUEL: PeripheralOwnerAbility<FuelAbility<*>> = PeripheralOwnerAbility()
        val OPERATION: PeripheralOwnerAbility<OperationAbility> = PeripheralOwnerAbility()
    }
}