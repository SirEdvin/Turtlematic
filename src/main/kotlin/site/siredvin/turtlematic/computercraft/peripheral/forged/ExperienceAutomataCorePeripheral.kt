package site.siredvin.turtlematic.computercraft.peripheral.forged

import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility.Companion.EXPERIENCE
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.lib.computercraft.peripheral.ability.ExperienceAbility
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral

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