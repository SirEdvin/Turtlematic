package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.plugins.*
import site.siredvin.turtlematic.util.toCreative
import site.siredvin.turtlematic.util.toNetherite
import site.siredvin.turtlematic.util.toStarbound

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

    companion object: PeripheralConfiguration {
        override val TYPE = "endAutomata"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEndAutomataCore
}