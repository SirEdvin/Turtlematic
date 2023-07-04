package site.siredvin.turtlematic.data

import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import site.siredvin.peripheralium.data.blocks.createFlatItem
import site.siredvin.peripheralium.data.blocks.turtleUpgrades
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.common.setup.Items

object ModItemModelProvider {

    val BASE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/base_gear")
    val NETHERITE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/netherite_gear")
    val STARBOUND_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/starbound_gear")
    val CREATIVE_GEAR = ResourceLocation(TurtlematicCore.MOD_ID, "item/creative_gear")
    val FIRE = ResourceLocation(TurtlematicCore.MOD_ID, "item/fire/")
    val BIG_FIRE = ResourceLocation(TurtlematicCore.MOD_ID, "item/big_fire/")

    fun createAutomataCore(
        generators: ItemModelGenerators,
        item: Item,
        gear: ResourceLocation,
        fire: String,
        fireTemplate: ResourceLocation = FIRE,
    ) {
        createFlatItem(
            generators,
            item,
            gear,
            fireTemplate.withSuffix(fire),
        )
    }

    fun addModels(generators: ItemModelGenerators) {
        generators.generateFlatItem(Items.TURTLE_CHATTER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.SOUL_SCRAPPER.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CREATIVE_CHEST.get(), ModelTemplates.FLAT_ITEM)
        generators.generateFlatItem(Items.CHUNK_VIAL.get(), ModelTemplates.FLAT_ITEM)

        turtleUpgrades(generators, Items.CHUNK_VIAL.get())
        turtleUpgrades(generators, Items.CREATIVE_CHEST.get())
        turtleUpgrades(generators, Items.TURTLE_CHATTER.get(), baseID = ResourceLocation(TurtlematicCore.MOD_ID, "turtle/chatter"))

        createAutomataCore(generators, Items.AUTOMATA_CORE.get(), BASE_GEAR, "base")
        createAutomataCore(generators, Items.HUSBANDRY_AUTOMATA_CORE.get(), BASE_GEAR, "green")
        createAutomataCore(generators, Items.END_AUTOMATA_CORE.get(), BASE_GEAR, "purple")
        createAutomataCore(generators, Items.PROTECTIVE_AUTOMATA_CORE.get(), BASE_GEAR, "red")
        createAutomataCore(generators, Items.ENORMOUS_AUTOMATA_CORE.get(), CREATIVE_GEAR, "enormous", BIG_FIRE)

        createAutomataCore(generators, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(), NETHERITE_GEAR, "green")
        createAutomataCore(generators, Items.NETHERITE_END_AUTOMATA_CORE.get(), NETHERITE_GEAR, "purple")
        createAutomataCore(generators, Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get(), NETHERITE_GEAR, "red")

        createAutomataCore(generators, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(), STARBOUND_GEAR, "green", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_END_AUTOMATA_CORE.get(), STARBOUND_GEAR, "purple", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE.get(), STARBOUND_GEAR, "red", BIG_FIRE)

        createAutomataCore(generators, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE.get(), CREATIVE_GEAR, "green", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_END_AUTOMATA_CORE.get(), CREATIVE_GEAR, "purple", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE.get(), CREATIVE_GEAR, "red", BIG_FIRE)

        createAutomataCore(generators, Items.ENCHANTING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "obsidian")
        createAutomataCore(generators, Items.BREWING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "light_blue")
        createAutomataCore(generators, Items.MASON_AUTOMATA_CORE.get(), NETHERITE_GEAR, "gray")
        createAutomataCore(generators, Items.SMITHING_AUTOMATA_CORE.get(), NETHERITE_GEAR, "base")
        createAutomataCore(generators, Items.MERCANTILE_AUTOMATA_CORE.get(), NETHERITE_GEAR, "brown")

        createAutomataCore(generators, Items.STARBOUND_ENCHANTING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "obsidian", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_BREWING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "light_blue", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_MASON_AUTOMATA_CORE.get(), STARBOUND_GEAR, "gray", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_SMITHING_AUTOMATA_CORE.get(), STARBOUND_GEAR, "base", BIG_FIRE)
        createAutomataCore(generators, Items.STARBOUND_MERCANTILE_AUTOMATA_CORE.get(), STARBOUND_GEAR, "brown", BIG_FIRE)

        createAutomataCore(generators, Items.CREATIVE_ENCHANTING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "obsidian", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_BREWING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "light_blue", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_MASON_AUTOMATA_CORE.get(), CREATIVE_GEAR, "gray", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_SMITHING_AUTOMATA_CORE.get(), CREATIVE_GEAR, "base", BIG_FIRE)
        createAutomataCore(generators, Items.CREATIVE_MERCANTILE_AUTOMATA_CORE.get(), CREATIVE_GEAR, "brown", BIG_FIRE)

        createFlatItem(generators, Items.FORGED_AUTOMATA_CORE.get(), NETHERITE_GEAR)
        createFlatItem(generators, Items.FILLED_SOUL_VIAL.get(), ResourceLocation(TurtlematicCore.MOD_ID, "item/soul_vial/full"))

        for (i in 1..3) {
            createFlatItem(
                generators,
                ModelLocationUtils.getModelLocation(Items.SOUL_VIAL.get()).withSuffix("_$i"),
                ResourceLocation(TurtlematicCore.MOD_ID, "item/soul_vial/phase$i"),
            )
        }
    }
}
