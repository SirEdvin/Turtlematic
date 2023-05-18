package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import site.siredvin.peripheralium.api.datatypes.AreaInteractionMode
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
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
        const val TYPE = "enormousAutomata"
        val UPGRADE_ID = ResourceLocation(TurtlematicCore.MOD_ID, TYPE)
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableEnormousAutomata
}