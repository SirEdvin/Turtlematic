package site.siredvin.turtlematic.common.setup

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.base.TurtleItem
import site.siredvin.turtlematic.integrations.computercraft.turtle.EndAutomata
import site.siredvin.turtlematic.integrations.computercraft.turtle.HusbandryAutomata

object Items {
    val AUTOMATA_CORE = AutomataCore()
    val HUSBANDRY_AUTOMATA_CORE = TurtleItem(HusbandryAutomata.ID) { TurtlematicConfig.enableHusbandryAutomataCore }
    val END_AUTOMATA_CORE = TurtleItem(EndAutomata.ID) { TurtlematicConfig.enableEndAutomataCore }

    fun register() {
        Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, "automata_core"), AUTOMATA_CORE)
        Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, "husbandry_automata_core"), HUSBANDRY_AUTOMATA_CORE)
        Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, "end_automata_core"), END_AUTOMATA_CORE)
    }
}