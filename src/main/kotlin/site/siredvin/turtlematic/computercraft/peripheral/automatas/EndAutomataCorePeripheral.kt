package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.plugins.*

class EndAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, tier
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