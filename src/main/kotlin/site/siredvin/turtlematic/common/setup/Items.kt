package site.siredvin.turtlematic.common.setup

import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.DescriptiveItem
import site.siredvin.turtlematic.common.items.base.TurtleItem
import site.siredvin.turtlematic.integrations.computercraft.turtle.*
import site.siredvin.turtlematic.util.register

object Items {
    val AUTOMATA_CORE = AutomataCore().register("automata_core")
    val HUSBANDRY_AUTOMATA_CORE =
        TurtleItem(HusbandryAutomata.ID) { TurtlematicConfig.enableHusbandryAutomataCore }.register("husbandry_automata_core")
    val END_AUTOMATA_CORE =
        TurtleItem(EndAutomata.ID) { TurtlematicConfig.enableEndAutomataCore }.register("end_automata_core")
    val ENORMOUS_AUTOMATA_CORE =
        TurtleItem(EnormousAutomata.ID) { TurtlematicConfig.enableEnormousAutomata }.register("enormous_automata_core")
    val SOUL_VIAL = SoulVial().register("soul_vial")
    val FILLED_SOUL_VIAL = DescriptiveItem().register("filled_soul_vial")
    val SOUL_SCRAPPER = TurtleItem(SoulScrapperTurtle.ID) { true }.register("soul_scrapper")
    val TURTLE_CHATTER = TurtleItem(ChatterTurtle.ID) { TurtlematicConfig.enableTurtleChatter }.register("turtle_chatter")
}