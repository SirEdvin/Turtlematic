package site.siredvin.turtlematic.computercraft

import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.tweakium.modules.peripheral.ability.TurtleFuelBoon
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner

class AutomataCoreFuelBoon(owner: TurtlePeripheralOwner, private val tier: IAutomataCoreTier) : TurtleFuelBoon(owner, tier.maxFuelConsumptionRate) {

    override val isFuelConsumptionDisable: Boolean
        get() = super.isFuelConsumptionDisable || tier.traits.contains(AutomataCoreTraits.FUEL_CONSUMPTION_DISABLED)
}
