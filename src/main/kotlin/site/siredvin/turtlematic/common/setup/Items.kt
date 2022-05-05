package site.siredvin.turtlematic.common.setup

import net.minecraft.world.item.Item
import site.siredvin.lib.api.TurtleIDBuildFunction
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.RecipeAutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.lib.common.items.DescriptiveItem
import site.siredvin.lib.common.items.TurtleItem
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.items.ForgedAutomataCore
import site.siredvin.turtlematic.computercraft.ComputerCraftProxy
import site.siredvin.turtlematic.computercraft.turtle.*
import site.siredvin.turtlematic.util.register

object Items {
    val ITEMS = mutableListOf<Item>()

    // Simple automata core
    val AUTOMATA_CORE = AutomataCore().register("automata_core")
    val HUSBANDRY_AUTOMATA_CORE =
        RecipeAutomataCore(AutomataCoreTier.TIER2) { TurtlematicConfig.enableHusbandryAutomataCore }.register("husbandry_automata_core")
    val END_AUTOMATA_CORE =
        RecipeAutomataCore(AutomataCoreTier.TIER2) { TurtlematicConfig.enableEndAutomataCore }.register("end_automata_core")
    val NETHERITE_HUSBANDRY_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.TIER3, { TurtlematicConfig.enableHusbandryAutomataCore }).register("netherite_husbandry_automata_core")
    val NETHERITE_END_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.TIER3, { TurtlematicConfig.enableEndAutomataCore }).register("netherite_end_automata_core")
    val STARBOUND_HUSBANDRY_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableHusbandryAutomataCore }).register("starbound_husbandry_automata_core")
    val STARBOUND_END_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableEndAutomataCore }).register("starbound_end_automata_core")
    val CREATIVE_HUSBANDRY_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableHusbandryAutomataCore }).register("creative_husbandry_automata_core")
    val CREATIVE_END_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEndAutomataCore }).register("creative_end_automata_core")
    val ENORMOUS_AUTOMATA_CORE =
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEnormousAutomata }).register("enormous_automata_core")

    // Forged automata cores

    val FORGED_AUTOMATA_CORE = ForgedAutomataCore().register("forged_automata_core")
    val BREWING_AUTOMATA_CORE = RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableBrewingAutomataCore}.register("brewing_automata_core")
    val SMITHING_AUTOMATA_CORE = RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableSmithingAutomataCore}.register("smithing_automata_core")
    val ENCHANTING_AUTOMATA_CORE = RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableEnchantingAutomataCore}.register("enchanting_automata_core")
    val MASON_AUTOMATA_CORE = RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableMasonAutomataCore}.register("mason_automata_core")

    val STARBOUND_BREWING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableBrewingAutomataCore}).register("starbound_brewing_automata_core")
    val STARBOUND_SMITHING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableSmithingAutomataCore}).register("starbound_smithing_automata_core")
    val STARBOUND_ENCHANTING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableEnchantingAutomataCore}).register("starbound_enchanting_automata_core")
    val STARBOUND_MASON_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableMasonAutomataCore}).register("starbound_mason_automata_core")

    val CREATIVE_BREWING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableBrewingAutomataCore}).register("creative_brewing_automata_core")
    val CREATIVE_SMITHING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableSmithingAutomataCore}).register("creative_smithing_automata_core")
    val CREATIVE_ENCHANTING_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEnchantingAutomataCore}).register("creative_enchanting_automata_core")
    val CREATIVE_MASON_AUTOMATA_CORE = BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableMasonAutomataCore}).register("creative_mason_automata_core")

    // Progression items
    val SOUL_VIAL = SoulVial().register("soul_vial")
    val FILLED_SOUL_VIAL = DescriptiveItem().register("filled_soul_vial")
    val SOUL_SCRAPPER = TurtleItem(Turtlematic.TAB, { true }).register("soul_scrapper")

    // miscellaneous
    val TURTLE_CHATTER = TurtleItem(Turtlematic.TAB, { TurtlematicConfig.enableTurtleChatter }, TurtleIDBuildFunction.WITHOUT_TURTLE).register("turtle_chatter")
    val CREATIVE_CHEST = TurtleItem(Turtlematic.TAB, { TurtlematicConfig.enableCreativeChest }) .register("creative_chest")
}