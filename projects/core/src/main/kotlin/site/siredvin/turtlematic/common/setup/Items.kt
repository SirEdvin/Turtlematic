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
import site.siredvin.turtlematic.xplat.ModPlatform

object Items {
    // Simple automata core
    val AUTOMATA_CORE = ModPlatform.registerItem("automata_core") { AutomataCore() }

    val HUSBANDRY_AUTOMATA_CORE =
        ModPlatform.registerItem("husbandry_automata_core") {
            RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableHusbandryAutomataCore, coreHook = capturedTooltip)
        }

    val END_AUTOMATA_CORE = ModPlatform.registerItem("end_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableEndAutomataCore, coreHook = capturedTooltip)
    }
    val PROTECTIVE_AUTOMATA_CORE = ModPlatform.registerItem("protective_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER2, TurtlematicConfig::enableProtectiveAutomataCore, protectiveTooltip, coreHook = capturedTooltip)
    }
    val NETHERITE_HUSBANDRY_AUTOMATA_CORE = ModPlatform.registerItem("netherite_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip, coreHook = capturedTooltip)
    }
    val NETHERITE_END_AUTOMATA_CORE = ModPlatform.registerItem("netherite_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip, coreHook = capturedTooltip)
    }
    val NETHERITE_PROTECTIVE_AUTOMATA_CORE = ModPlatform.registerItem("netherite_protective_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableProtectiveAutomataCore, itemUsageTooltip, protectiveTooltip, coreHook = capturedTooltip)
    }
    val STARBOUND_HUSBANDRY_AUTOMATA_CORE = ModPlatform.registerItem("starbound_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip, coreHook = capturedTooltip)
    }
    val STARBOUND_END_AUTOMATA_CORE = ModPlatform.registerItem("starbound_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip, coreHook = capturedTooltip)
    }
    val STARBOUND_PROTECTIVE_AUTOMATA_CORE = ModPlatform.registerItem("starbound_protective_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableProtectiveAutomataCore, itemUsageTooltip, protectiveTooltip, coreHook = capturedTooltip)
    }
    val CREATIVE_HUSBANDRY_AUTOMATA_CORE = ModPlatform.registerItem("creative_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableHusbandryAutomataCore, itemUsageTooltip, husbandryTooltip, coreHook = capturedTooltip)
    }
    val CREATIVE_END_AUTOMATA_CORE = ModPlatform.registerItem("creative_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEndAutomataCore, itemUsageTooltip, coreHook = capturedTooltip)
    }
    val CREATIVE_PROTECTIVE_AUTOMATA_CORE = ModPlatform.registerItem("creative_protective_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableProtectiveAutomataCore, itemUsageTooltip, protectiveTooltip, coreHook = capturedTooltip)
    }
    val ENORMOUS_AUTOMATA_CORE = ModPlatform.registerItem("enormous_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEnormousAutomata, itemUsageTooltip, coreHook = capturedTooltip)
    }

    // Forged automata cores

    val FORGED_AUTOMATA_CORE = ModPlatform.registerItem("forged_automata_core") { ForgedAutomataCore() }
    val BREWING_AUTOMATA_CORE = ModPlatform.registerItem("brewing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip, coreHook = xpTooltip)
    }
    val SMITHING_AUTOMATA_CORE = ModPlatform.registerItem("smithing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableSmithingAutomataCore, coreHook = xpTooltip)
    }
    val ENCHANTING_AUTOMATA_CORE = ModPlatform.registerItem("enchanting_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip, coreHook = xpTooltip)
    }
    val MASON_AUTOMATA_CORE = ModPlatform.registerItem("mason_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableMasonAutomataCore, coreHook = xpTooltip)
    }
    val MERCANTILE_AUTOMATA_CORE = ModPlatform.registerItem("mercantile_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip, coreHook = xpTooltip)
    }

    val STARBOUND_BREWING_AUTOMATA_CORE = ModPlatform.registerItem("starbound_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip, coreHook = xpTooltip)
    }
    val STARBOUND_SMITHING_AUTOMATA_CORE = ModPlatform.registerItem("starbound_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableSmithingAutomataCore, coreHook = xpTooltip)
    }
    val STARBOUND_ENCHANTING_AUTOMATA_CORE = ModPlatform.registerItem("starbound_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip, coreHook = xpTooltip)
    }
    val STARBOUND_MASON_AUTOMATA_CORE = ModPlatform.registerItem("starbound_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableMasonAutomataCore, coreHook = xpTooltip)
    }
    val STARBOUND_MERCANTILE_AUTOMATA_CORE = ModPlatform.registerItem("starbound_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip, coreHook = xpTooltip)
    }

    val CREATIVE_BREWING_AUTOMATA_CORE = ModPlatform.registerItem("creative_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableBrewingAutomataCore, itemUsageTooltip, coreHook = xpTooltip)
    }
    val CREATIVE_SMITHING_AUTOMATA_CORE = ModPlatform.registerItem("creative_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableSmithingAutomataCore, coreHook = xpTooltip)
    }
    val CREATIVE_ENCHANTING_AUTOMATA_CORE = ModPlatform.registerItem("creative_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableEnchantingAutomataCore, enchantingTooltip, coreHook = xpTooltip)
    }
    val CREATIVE_MASON_AUTOMATA_CORE = ModPlatform.registerItem("creative_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableMasonAutomataCore, coreHook = xpTooltip)
    }
    val CREATIVE_MERCANTILE_AUTOMATA_CORE = ModPlatform.registerItem("creative_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, TurtlematicConfig::enableMercantileAutomataCore, tradingTooltip, coreHook = xpTooltip)
    }

    // Progression items
    val SOUL_VIAL = ModPlatform.registerItem("soul_vial") { SoulVial() }

    val FILLED_SOUL_VIAL = ModPlatform.registerItem("filled_soul_vial") { DescriptiveItem(Item.Properties()) }

    val SOUL_SCRAPPER = ModPlatform.registerItem("soul_scrapper") { PeripheralItem(Item.Properties(), { true }) }

    // miscellaneous
    val TURTLE_CHATTER = ModPlatform.registerItem("turtle_chatter") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableTurtleChatter,
            alwaysShow = true,
            isDisabled,
        )
    }

    val MIMIC_GADGET = ModPlatform.registerItem("mimic_gadget") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableMimicGadget,
            alwaysShow = true,
            isDisabled,
        )
    }

    val CREATIVE_CHEST = ModPlatform.registerItem("creative_chest") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableCreativeChest,
            alwaysShow = true,
            isDisabled,
        )
    }
    val CHUNK_VIAL = ModPlatform.registerItem("chunk_vial") {
        PeripheralItem(
            Item.Properties(),
            TurtlematicConfig::enableChunkVial,
            alwaysShow = true,
            isDisabled,
        )
    }

    fun doSomething() {}
}
