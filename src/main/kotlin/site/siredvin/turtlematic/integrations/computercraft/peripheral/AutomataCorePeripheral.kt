package site.siredvin.turtlematic.integrations.computercraft.peripheral

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.apcode.plugins.AutomataBlockHandPlugin
import site.siredvin.apcode.plugins.AutomataItemSuckPlugin
import site.siredvin.apcode.plugins.AutomataLookPlugin
import site.siredvin.apcode.plugins.AutomataSoulFeedingPlugin
import site.siredvin.lib.operations.AutomataCoreTier
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class AutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER1
){
    init {
        addPlugin(AutomataItemSuckPlugin(this))
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataBlockHandPlugin(this))
        addPlugin(AutomataSoulFeedingPlugin(this))
    }

    companion object {
        const val TYPE = "automata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableAutomataCore
}