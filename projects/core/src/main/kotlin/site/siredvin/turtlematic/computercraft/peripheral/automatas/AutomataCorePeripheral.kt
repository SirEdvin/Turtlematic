package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.plugins.AutomataInteractionPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataLookPlugin
import site.siredvin.turtlematic.computercraft.plugins.AutomataScanPlugin

class AutomataCorePeripheral(
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
): BaseAutomataCorePeripheral(
    TYPE, turtle, side, tier
){
    init {
        addPlugin(AutomataLookPlugin(this))
        addPlugin(AutomataInteractionPlugin(this))
        addPlugin(AutomataScanPlugin(this))
    }

    companion object {
        const val TYPE = "automata"
        val UPGRADE_ID = ResourceLocation(TurtlematicCore.MOD_ID, TYPE)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableAutomataCore
}