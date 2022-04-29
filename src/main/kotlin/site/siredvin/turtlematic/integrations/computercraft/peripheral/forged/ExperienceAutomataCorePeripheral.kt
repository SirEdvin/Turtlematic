package site.siredvin.turtlematic.integrations.computercraft.peripheral.forged

import site.siredvin.lib.peripherals.ability.PeripheralOwnerAbility.Companion.EXPERIENCE
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.lib.peripherals.ability.ExperienceAbility
import site.siredvin.turtlematic.integrations.computercraft.peripheral.automatas.BaseAutomataCorePeripheral

abstract class ExperienceAutomataCorePeripheral(
    type: String,
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
) : BaseAutomataCorePeripheral(
    type, turtle, side, tier
) {
    init {
        peripheralOwner.attachAbility(EXPERIENCE, ExperienceAbility(peripheralOwner, tier.interactionRadius))
    }
}