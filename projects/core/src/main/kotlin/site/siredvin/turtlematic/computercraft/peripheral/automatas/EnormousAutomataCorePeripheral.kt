package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.ScanningAbility
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SphereOperation
import site.siredvin.turtlematic.computercraft.plugins.*

class EnormousAutomataCorePeripheral(
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
        addPlugin(
            AutomataInteractionPlugin(
                this,
                suitableEntity = { true },
                allowedMods = InteractionMode.values().toSet(),
            ),
        )
        peripheralOwner.attachAbility(
            PeripheralOwnerAbility.SCANNING,
            ScanningAbility(
                peripheralOwner,
                tier.interactionRadius,
            ).attachItemScan(SphereOperation.SCAN_ITEMS).attachBlockScan(
                SphereOperation.SCAN_BLOCKS,
            ).attachLivingEntityScan(SphereOperation.SCAN_ENTITIES, { true }),
        )
        addPlugin(
            AutomataCapturePlugin(
                this,
                suitableEntity = { true },
                allowedMods = InteractionMode.values().toSet(),
            ),
        )
        addPlugin(AutomataWarpingPlugin(this))
    }

    companion object : PeripheralConfiguration {
        override val TYPE = "enormousAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnormousAutomata
}
