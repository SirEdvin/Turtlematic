package site.siredvin.turtlematic.common.setup

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.BaseItem
import site.siredvin.turtlematic.common.items.base.DescriptiveItem
import site.siredvin.turtlematic.common.items.base.TurtleItem
import site.siredvin.turtlematic.integrations.computercraft.turtle.EndAutomata
import site.siredvin.turtlematic.integrations.computercraft.turtle.EnormousAutomata
import site.siredvin.turtlematic.integrations.computercraft.turtle.HusbandryAutomata

fun <T: BaseItem> T.register(name: String): T {
    Registry.register(Registry.ITEM, ResourceLocation(Turtlematic.MOD_ID, name), this)
    return this
}

object Items {
    val AUTOMATA_CORE = AutomataCore().register("automata_core")
    val HUSBANDRY_AUTOMATA_CORE = TurtleItem(HusbandryAutomata.ID) { TurtlematicConfig.enableHusbandryAutomataCore }.register("husbandry_automata_core")
    val END_AUTOMATA_CORE = TurtleItem(EndAutomata.ID) { TurtlematicConfig.enableEndAutomataCore }.register("end_automata_core")
    val ENORMOUS_AUTOMATA_CORE = TurtleItem(EnormousAutomata.ID) { TurtlematicConfig.enableEnormousAutomata }.register("enormous_automata_core")
    val SOUL_VIAL = SoulVial().register("soul_vial")
    val FILLED_SOUL_VIAL = DescriptiveItem().register("filled_soul_vial")

    fun register() {
    }
}