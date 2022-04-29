package site.siredvin.turtlematic.integrations.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.integrations.computercraft.plugins.*

class EndAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER2
){
    init {
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataInteractionPlugin(this))
        addPlugin(AutomataWarpingPlugin(this))
        addPlugin(AutomataScanPlugin(this))
        addPlugin(AutomataCapturePlugin(this, allowedMods = setOf(InteractionMode.BLOCK)))
    }

    companion object {
        const val TYPE = "endAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEndAutomataCore
}