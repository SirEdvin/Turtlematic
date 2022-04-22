package site.siredvin.turtlematic.integrations.computercraft.peripheral

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.apcode.plugins.*
import site.siredvin.lib.operations.AutomataCoreTier
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig

class EndAutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, AutomataCoreTier.TIER2
){
    init {
        addPlugin(AutomataItemSuckPlugin(this))
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataHandPlugin(this))
        addPlugin(AutomataWarpingPlugin(this))
    }

    companion object {
        const val TYPE = "endAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEndAutomataCore
}