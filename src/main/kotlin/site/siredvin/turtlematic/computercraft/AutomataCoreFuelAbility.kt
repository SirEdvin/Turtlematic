package site.siredvin.turtlematic.computercraft

import site.siredvin.lib.computercraft.peripheral.ability.TurtleFuelAbility
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier

class AutomataCoreFuelAbility(owner: TurtlePeripheralOwner, private val tier: IAutomataCoreTier) :
    TurtleFuelAbility(owner, tier.maxFuelConsumptionRate) {

    override val isFuelConsumptionDisable: Boolean
        get() = super.isFuelConsumptionDisable || tier.traits.contains(AutomataCoreTraits.FUEL_CONSUMPTION_DISABLED)
}