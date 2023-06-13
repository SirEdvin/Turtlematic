package site.siredvin.turtlematic.computercraft.peripheral.forged

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.computercraft.peripheral.ability.ExperienceAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility.Companion.EXPERIENCE
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.UnconditionalOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.plugins.AutomataItemSuckPlugin

abstract class ExperienceAutomataCorePeripheral(
    type: String,
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : BaseAutomataCorePeripheral(
    type,
    turtle,
    side,
    tier,
) {
    init {
        peripheralOwner.attachAbility(EXPERIENCE, ExperienceAbility(peripheralOwner, tier.interactionRadius, TurtlematicConfig.xpToFuelRate, UnconditionalOperation.XP_TRANSFER))
        addPlugin(AutomataItemSuckPlugin(this))
    }
}
