package site.siredvin.turtlematic.data

import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import site.siredvin.broccolium.modules.data.model.createFlatItem
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.tweakium.modules.data.turtleUpgrades

object ModItemModelProvider {

    const val BASE_GEAR = "base"
    const val NETHERITE_GEAR = "nether"
    const val STARBOUND_GEAR = "starbound"
    const val CREATIVE_GEAR = "creative"

    fun createAutomataCore(
        generators: ItemModelGenerators,
        item: Item,
        gear: String,
        fire: String,
    ) {
        createFlatItem(
            generators,
            item,
            ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "item/automata/${fire}_$gear"),
        )
    }

    fun addModels(generators: ItemModelGenerators) {
        generators.generateFlatItem(Items.TURTLE_CHATTER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.SOUL_SCRAPPER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CREATIVE_CHEST.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CHUNK_VIAL.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.MIMIC_GADGET.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.INSPECTION_MONOCLE.get(), ModelTemplates.FLAT_ITEM)

        turtleUpgrades(generators, Items.CHUNK_VIAL.get())
        turtleUpgrades(generators, Items.CREATIVE_CHEST.get())
        turtleUpgrades(generators, Items.TURTLE_CHATTER.get(), baseID = ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "turtle/chatter"))
        turtleUpgrades(generators, Items.MIMIC_GADGET.get(), baseID = ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "turtle/mimic"))

        createAutomataCore(generators, Items.AUTOMATA_CORE.get(), BASE_GEAR, "orange")
        createAutomataCore(generators, Items.HUSBANDRY_AUTOMATA_CORE.get(), BASE_GEAR, "green")
        createAutomataCore(generators, Items.END_AUTOMATA_CORE.get(), BASE_GEAR, "purple")
        createAutomataCore(generators, Items.PROTECTIVE_AUTOMATA_CORE.get(), BASE_GEAR, "red")
        createAutomataCore(generators, Items.ENORMOUS_AUTOMATA_CORE.get(), CREATIVE_GEAR, "dark_red")

        createAutomataCore(generators, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(), NETHERITE_GEAR, "green")
        createAutomataCore(generators, Items.NETHERITE_END_AUTOMATA_CORE.get(), NETHERITE_GEAR, "purple")
        createAutomataCore(generators, Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get(), NETHERITE_GEAR, "red")

        createAutomataCore(generators, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(), STARBOUND_GEAR, "green")
        createAutomataCore(generators, Items.STARBOUND_END_AUTOMATA_CORE.get(), STARBOUND_GEAR, "purple")
        createAutomataCore(generators, Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE.get(), STARBOUND_GEAR, "red")

        createAutomataCore(generators, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE.get(), CREATIVE_GEAR, "green")
        createAutomataCore(generators, Items.CREATIVE_END_AUTOMATA_CORE.get(), CREATIVE_GEAR, "purple")
        createAutomataCore(generators, Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE.get(), CREATIVE_GEAR, "red")

        createAutomataCore(generators, Items.ENCHANTING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "obsidian")
        createAutomataCore(generators, Items.BREWING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "light_blue")
        createAutomataCore(generators, Items.MASON_AUTOMATA_CORE.get(), NETHERITE_GEAR, "gray")
        createAutomataCore(generators, Items.SMITHING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "orange")
        createAutomataCore(generators, Items.MERCANTILE_AUTOMATA_CORE.get(), NETHERITE_GEAR, "brown")

        createAutomataCore(generators, Items.STARBOUND_ENCHANTING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "obsidian")
        createAutomataCore(generators, Items.STARBOUND_BREWING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "light_blue")
        createAutomataCore(generators, Items.STARBOUND_MASON_AUTOMATA_CORE.get(), STARBOUND_GEAR, "gray")
        createAutomataCore(generators, Items.STARBOUND_SMITHING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "orange")
        createAutomataCore(generators, Items.STARBOUND_MERCANTILE_AUTOMATA_CORE.get(), STARBOUND_GEAR, "brown")

        createAutomataCore(generators, Items.CREATIVE_ENCHANTING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "obsidian")
        createAutomataCore(generators, Items.CREATIVE_BREWING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "light_blue")
        createAutomataCore(generators, Items.CREATIVE_MASON_AUTOMATA_CORE.get(), CREATIVE_GEAR, "gray")
        createAutomataCore(generators, Items.CREATIVE_SMITHING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "orange")
        createAutomataCore(generators, Items.CREATIVE_MERCANTILE_AUTOMATA_CORE.get(), CREATIVE_GEAR, "brown")

        createFlatItem(generators, Items.FORGED_AUTOMATA_CORE.get(), ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "item/netherite_gear"))
        createFlatItem(generators, Items.FILLED_SOUL_VIAL.get(), ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "item/soul_vial/full"))

        for (i in 1..3) {
            createFlatItem(
                generators,
                ModelLocationUtils.getModelLocation(Items.SOUL_VIAL.get()).withSuffix("_$i"),
                ResourceLocation.fromNamespaceAndPath(TurtlematicCore.MOD_ID, "item/soul_vial/phase$i"),
            )
        }
    }
}
