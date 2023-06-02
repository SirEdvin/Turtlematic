package site.siredvin.turtlematic.common.setup

import net.minecraft.world.item.Item
import site.siredvin.peripheralium.common.items.DescriptiveItem
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.ForgedAutomataCore
import site.siredvin.turtlematic.common.items.RecipeAutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.util.*
import site.siredvin.turtlematic.xplat.TurtlematicPlatform

object Items {
    // Simple automata core
    val AUTOMATA_CORE = TurtlematicPlatform.registerItem("automata_core") { AutomataCore() }

    val HUSBANDRY_AUTOMATA_CORE =
        TurtlematicPlatform.registerItem("husbandry_automata_core") {
            RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableHusbandryAutomataCore)
        }

    val END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("end_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableEndAutomataCore)
    }
    val FLUIDY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("fluidy_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableFluidyAutomataCore)
    }
    val NETHERITE_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("netherite_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip)
    }
    val NETHERITE_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("netherite_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip)
    }
    val NETHERITE_FLUIDY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("netherite_fluidy_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableFluidyAutomataCore, itemUsageTooltip)
    }
    val STARBOUND_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip)
    }
    val STARBOUND_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip)
    }
    val STARBOUND_FLUIDY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_fluidy_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableFluidyAutomataCore, itemUsageTooltip)
    }
    val CREATIVE_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip)
    }
    val CREATIVE_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip)
    }
    val CREATIVE_FLUIDY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_fluidy_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableFluidyAutomataCore, itemUsageTooltip)
    }
    val ENORMOUS_AUTOMATA_CORE = TurtlematicPlatform.registerItem("enormous_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEnormousAutomata, itemUsageTooltip)
    }

    // Forged automata cores

    val FORGED_AUTOMATA_CORE = TurtlematicPlatform.registerItem("forged_automata_core") { ForgedAutomataCore() }
    val BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("brewing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip)
    }
    val SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("smithing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableSmithingAutomataCore)
    }
    val ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("enchanting_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip)
    }
    val MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("mason_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableMasonAutomataCore)
    }
    val MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("mercantile_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip)
    }

    val STARBOUND_BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip)
    }
    val STARBOUND_SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableSmithingAutomataCore)
    }
    val STARBOUND_ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip)
    }
    val STARBOUND_MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableMasonAutomataCore)
    }
    val STARBOUND_MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip)
    }

    val CREATIVE_BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip)
    }
    val CREATIVE_SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableSmithingAutomataCore)
    }
    val CREATIVE_ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip)
    }
    val CREATIVE_MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableMasonAutomataCore)
    }
    val CREATIVE_MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip)
    }

    // Progression items
    val SOUL_VIAL = TurtlematicPlatform.registerItem("soul_vial") { SoulVial() }

    val FILLED_SOUL_VIAL = TurtlematicPlatform.registerItem("filled_soul_vial") { DescriptiveItem(Item.Properties()) }

    val SOUL_SCRAPPER = TurtlematicPlatform.registerItem("soul_scrapper") { PeripheralItem(Item.Properties(), { true }) }

    // miscellaneous
    val TURTLE_CHATTER = TurtlematicPlatform.registerItem("turtle_chatter") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableTurtleChatter,
            alwaysShow = true,
            isDisabled,
        )
    }
    val CREATIVE_CHEST = TurtlematicPlatform.registerItem("creative_chest") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableCreativeChest,
            alwaysShow = true,
            isDisabled,
        )
    }
    val CHUNK_VIAL = TurtlematicPlatform.registerItem("chunk_vial") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableChunkVial,
            alwaysShow = true,
            isDisabled,
        )
    }

    fun doSomething() {}
}
