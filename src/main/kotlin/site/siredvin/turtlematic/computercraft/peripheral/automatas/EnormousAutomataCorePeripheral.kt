package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.AreaInteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.plugins.*

class EnormousAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, tier
){
    init {
        addPlugin(AutomataLookPlugin(this))
        addPlugin(
            AutomataInteractionPlugin(
            this,
            suitableEntity = { true },
            allowedMods = InteractionMode.values().toSet()
        )
        )
        addPlugin(
            AutomataScanPlugin(
            this,
            suitableEntity = { true },
            allowedMods = AreaInteractionMode.values().toSet()
        )
        )
        addPlugin(AutomataCapturePlugin(
            this,
            suitableEntity = { true },
            allowedMods = InteractionMode.values().toSet()
        ))
        addPlugin(AutomataWarpingPlugin(this))
    }

    companion object {
        const val TYPE = "automata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnormousAutomata
}