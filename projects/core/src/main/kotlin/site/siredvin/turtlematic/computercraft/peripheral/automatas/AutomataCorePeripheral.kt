package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.AutomataInteractionPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataItemSuckPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin
import site.siredvin.tweakium.modules.peripheral.ability.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.ability.ScanningBoon

class AutomataCorePeripheral(
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
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataInteractionPlugin(this))
        addPlugin(AutomataItemSuckPlugin(this))
        peripheralOwner.attachBoon(
            PeripheralOwnerBoonKey.SCANNING,
            ScanningBoon(
                peripheralOwner,
                tier.interactionRadius,
            ).attachItemScan(SphereOperation.SCAN_ITEMS),
        )
    }

    companion object : PeripheralConfiguration {
        override val type = "automata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableAutomataCore
}
