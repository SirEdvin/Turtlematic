package site.siredvin.lib.computercraft.peripheral.owner

import site.siredvin.lib.api.peripheral.IOwnerAbility
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.api.peripheral.IPeripheralOwner

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
        @Suppress("UNCHECKED_CAST")
        return _abilities[ability] as T
    }
}