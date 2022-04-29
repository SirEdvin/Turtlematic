package site.siredvin.lib.peripherals.owner

import site.siredvin.lib.peripherals.api.IOwnerAbility
import site.siredvin.lib.peripherals.ability.PeripheralOwnerAbility
import site.siredvin.lib.peripherals.api.IPeripheralOwner

abstract class BasePeripheralOwner : IPeripheralOwner {
    private val _abilities: MutableMap<PeripheralOwnerAbility<*>, IOwnerAbility>

    init {
        _abilities = HashMap()
    }

    override val abilities: Collection<IOwnerAbility>
        get() = _abilities.values

    override fun <T : IOwnerAbility> attachAbility(ability: PeripheralOwnerAbility<T>, abilityImplementation: T) {
        _abilities[ability] = abilityImplementation
    }

    override fun <T : IOwnerAbility> getAbility(ability: PeripheralOwnerAbility<T>): T? {
        return _abilities[ability] as T
    }
}