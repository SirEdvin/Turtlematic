package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.ScanningAbility
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.*

class FluidyAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : BaseAutomataCorePeripheral(
    TYPE,
    turtle,
    side,
    tier,
) {
    init {
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataInteractionPlugin(this))
        peripheralOwner.attachAbility(
            PeripheralOwnerAbility.SCANNING,
            ScanningAbility(
                peripheralOwner,
                tier.interactionRadius,
            ).attachItemScan(SphereOperation.SCAN_ITEMS).attachBlockScan(
                SphereOperation.SCAN_BLOCKS,
            ),
        )
    }

    companion object : PeripheralConfiguration {
        override val TYPE = "fluidyAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableFluidyAutomataCore
}
