package site.siredvin.turtlematic.common.setup

import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.RecipeAutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.lib.items.DescriptiveItem
import site.siredvin.lib.items.TurtleItem
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.ForgedAutomataCore
import site.siredvin.turtlematic.integrations.computercraft.turtle.*
import site.siredvin.turtlematic.util.register

object Items {
    // Simple automata core
    val AUTOMATA_CORE = AutomataCore().register("automata_core")
    val HUSBANDRY_AUTOMATA_CORE =
        RecipeAutomataCore(AutomataCoreTier.TIER2, HusbandryAutomata.ID) { TurtlematicConfig.enableHusbandryAutomataCore }.register("husbandry_automata_core")
    val END_AUTOMATA_CORE =
        RecipeAutomataCore(AutomataCoreTier.TIER2, EndAutomata.ID) { TurtlematicConfig.enableEndAutomataCore }.register("end_automata_core")
    val ENORMOUS_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.ENORMOUS_TIER, EnormousAutomata.ID) { TurtlematicConfig.enableEnormousAutomata }.register("enormous_automata_core")

    // Forged automata cores

    val FORGED_AUTOMATA_CORE = ForgedAutomataCore().register("forged_automata_core")
    val BREWING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.TIER3, BrewingAutomata.ID) { TurtlematicConfig.enableBrewingAutomataCore}.register("brewing_automata_core")

    // Progression items
    val SOUL_VIAL = SoulVial().register("soul_vial")
    val FILLED_SOUL_VIAL = DescriptiveItem().register("filled_soul_vial")
    val SOUL_SCRAPPER = TurtleItem(Turtlematic.TAB, SoulScrapperTurtle.ID) { true }.register("soul_scrapper")

    // miscellaneous
    val TURTLE_CHATTER = TurtleItem(Turtlematic.TAB, ChatterTurtle.ID) { TurtlematicConfig.enableTurtleChatter }.register("turtle_chatter")
    val CREATIVE_CHEST = TurtleItem(Turtlematic.TAB, CreativeChestTurtle.ID) { TurtlematicConfig.enableCreativeChest }.register("creative_chest")
}