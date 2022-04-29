package site.siredvin.turtlematic.integrations.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.integrations.computercraft.plugins.AutomataInteractionPlugin
import site.siredvin.turtlematic.integrations.computercraft.plugins.AutomataLookPlugin
import site.siredvin.turtlematic.integrations.computercraft.plugins.AutomataScanPlugin

class AutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER1
){
    init {
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataInteractionPlugin(this))
        addPlugin(AutomataScanPlugin(this))
    }

    companion object {
        const val TYPE = "automata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableAutomataCore
}