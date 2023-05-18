package site.siredvin.turtlematic.common.setup

import net.minecraft.world.item.Item
import site.siredvin.peripheralium.api.TurtleIDBuildFunction
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.AutomataCore
import site.siredvin.turtlematic.common.items.RecipeAutomataCore
import site.siredvin.turtlematic.common.items.SoulVial
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.peripheralium.common.items.DescriptiveItem
import site.siredvin.peripheralium.common.items.TurtleItem
import site.siredvin.turtlematic.common.items.ForgedAutomataCore
import site.siredvin.turtlematic.xplat.TurtlematicPlatform

object Items {
    // Simple automata core
    val AUTOMATA_CORE = TurtlematicPlatform.registerItem("automata_core") { AutomataCore() }

    val HUSBANDRY_AUTOMATA_CORE =
        TurtlematicPlatform.registerItem("husbandry_automata_core") { RecipeAutomataCore(AutomataCoreTier.TIER2) { TurtlematicConfig.enableHusbandryAutomataCore } }

    val END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("end_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER2) { TurtlematicConfig.enableEndAutomataCore }
    }
    val NETHERITE_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("netherite_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, { TurtlematicConfig.enableHusbandryAutomataCore })
    }
    val NETHERITE_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("netherite_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER3, { TurtlematicConfig.enableEndAutomataCore })
    }
    val STARBOUND_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableHusbandryAutomataCore })
    }
    val STARBOUND_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableEndAutomataCore })
    }
    val CREATIVE_HUSBANDRY_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_husbandry_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableHusbandryAutomataCore })
    }
    val CREATIVE_END_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_end_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEndAutomataCore })
    }
    val ENORMOUS_AUTOMATA_CORE = TurtlematicPlatform.registerItem("enormous_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEnormousAutomata })
    }

    // Forged automata cores

    val FORGED_AUTOMATA_CORE = TurtlematicPlatform.registerItem("forged_automata_core") { ForgedAutomataCore() }
    val BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("brewing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableBrewingAutomataCore }
    }
    val SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("smithing_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableSmithingAutomataCore }
    }
    val ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("enchanting_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableEnchantingAutomataCore }
    }
    val MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("mason_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableMasonAutomataCore }
    }
    val MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("mercantile_automata_core") {
        RecipeAutomataCore(AutomataCoreTier.TIER3) { TurtlematicConfig.enableMercantileAutomataCore }
    }

    val STARBOUND_BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableBrewingAutomataCore })
    }
    val STARBOUND_SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableSmithingAutomataCore })
    }
    val STARBOUND_ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableEnchantingAutomataCore })
    }
    val STARBOUND_MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableMasonAutomataCore })
    }
    val STARBOUND_MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("starbound_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.TIER4, { TurtlematicConfig.enableMercantileAutomataCore })
    }

    val CREATIVE_BREWING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_brewing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableBrewingAutomataCore })
    }
    val CREATIVE_SMITHING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_smithing_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableSmithingAutomataCore })
    }
    val CREATIVE_ENCHANTING_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_enchanting_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableEnchantingAutomataCore })
    }
    val CREATIVE_MASON_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_mason_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableMasonAutomataCore })
    }
    val CREATIVE_MERCANTILE_AUTOMATA_CORE = TurtlematicPlatform.registerItem("creative_mercantile_automata_core") {
        BaseAutomataCore(AutomataCoreTier.CREATIVE, { TurtlematicConfig.enableMercantileAutomataCore })
    }

    // Progression items
    val SOUL_VIAL = TurtlematicPlatform.registerItem("soul_vial") { SoulVial() }

    val FILLED_SOUL_VIAL = TurtlematicPlatform.registerItem("filled_soul_vial") { DescriptiveItem(Item.Properties()) }

    val SOUL_SCRAPPER = TurtlematicPlatform.registerItem("soul_scrapper") { TurtleItem(Item.Properties(), { true }) }

    // miscellaneous
    val TURTLE_CHATTER = TurtlematicPlatform.registerItem("turtle_chatter") {
        TurtleItem(
            Item.Properties(),
            { TurtlematicConfig.enableTurtleChatter },
            TurtleIDBuildFunction.WITHOUT_TURTLE
        )
    }
    val CREATIVE_CHEST = TurtlematicPlatform.registerItem("creative_chest") {
        TurtleItem(
            Item.Properties(),
            { TurtlematicConfig.enableCreativeChest })
    }
    val CHUNK_VIAL = TurtlematicPlatform.registerItem("chunk_vial") {
        TurtleItem(
            Item.Properties(),
            { TurtlematicConfig.enableChunkVial })
    }

    fun doSomething() {}
}