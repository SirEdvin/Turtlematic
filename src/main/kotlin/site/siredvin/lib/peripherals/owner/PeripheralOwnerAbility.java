package site.siredvin.lib.peripherals.owner;

@SuppressWarnings("InstantiationOfUtilityClass")
public class PeripheralOwnerAbility<T> {
    public static final PeripheralOwnerAbility<FuelAbility<?>> FUEL = new PeripheralOwnerAbility<FuelAbility<?>>();
    public static final PeripheralOwnerAbility<OperationAbility> OPERATION = new PeripheralOwnerAbility<OperationAbility>();

    public PeripheralOwnerAbility(){}
}
