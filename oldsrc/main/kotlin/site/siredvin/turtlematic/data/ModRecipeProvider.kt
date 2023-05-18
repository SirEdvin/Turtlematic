package site.siredvin.turtlematic.data

import dan200.computercraft.shared.Registry
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.UpgradeRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.peripheralium.data.TweakedShapedRecipeBuilder
import site.siredvin.peripheralium.data.TweakedUpgradeRecipeBuilder
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import java.util.*
import java.util.function.Consumer

class ModRecipeProvider(dataGenerator: FabricDataGenerator) : FabricRecipeProvider(dataGenerator) {

    fun generateShapedRecipes(consumer: Consumer<FinishedRecipe>) {
        TweakedShapedRecipeBuilder.shaped(Items.SOUL_VIAL)
            .define('S', net.minecraft.world.item.Items.SOUL_LANTERN)
            .define('E', ConventionalItemTags.EMERALDS)
            .define('R', ConventionalItemTags.REDSTONE_DUSTS)
            .pattern("R R")
            .pattern("ESE")
            .pattern("R R")
            .group(Turtlematic.MOD_ID)
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.AUTOMATA_CORE)
            .define('/', net.minecraft.world.item.Items.STICK)
            .define('S', Items.FILLED_SOUL_VIAL)
            .define('D', ConventionalItemTags.DIAMONDS)
            .define('I', ConventionalItemTags.IRON_INGOTS)
            .pattern("/I/")
            .pattern("DSD")
            .pattern("/I/")
            .group(Turtlematic.MOD_ID)
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.SOUL_SCRAPPER)
            .define('/', net.minecraft.world.item.Items.STICK)
            .define('S', net.minecraft.world.item.Items.SOUL_LANTERN)
            .pattern(" S ")
            .pattern("S/S")
            .pattern("/  ")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.TURTLE_CHATTER)
            .define('S', Registry.ModBlocks.SPEAKER)
            .define('R', ConventionalItemTags.REDSTONE_DUSTS)
            .define('I', ConventionalItemTags.IRON_INGOTS)
            .pattern("RIR")
            .pattern("ISI")
            .pattern("RIR")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.CHUNK_VIAL)
            .define('S', Items.FILLED_SOUL_VIAL)
            .define('G', ConventionalItemTags.GOLD_INGOTS)
            .define('D', ConventionalItemTags.DIAMONDS)
            .define('E', ConventionalItemTags.EMERALDS)
            .pattern("GDG")
            .pattern("GSG")
            .pattern("GEG")
            .save(consumer)
    }

    fun generateSmithingRecipes(consumer: Consumer<FinishedRecipe>) {
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.AUTOMATA_CORE), Ingredient.of(ConventionalItemTags.NETHERITE_INGOTS),
            Items.FORGED_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.HUSBANDRY_AUTOMATA_CORE), Ingredient.of(ConventionalItemTags.NETHERITE_INGOTS),
            Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.END_AUTOMATA_CORE), Ingredient.of(ConventionalItemTags.NETHERITE_INGOTS),
            Items.NETHERITE_END_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_END_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_END_AUTOMATA_CORE
        ).save(consumer)

        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.BREWING_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_BREWING_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.ENCHANTING_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_ENCHANTING_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.SMITHING_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_SMITHING_AUTOMATA_CORE
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.MASON_AUTOMATA_CORE), Ingredient.of(net.minecraft.world.item.Items.NETHER_STAR),
            Items.STARBOUND_MASON_AUTOMATA_CORE
        ).save(consumer)
    }

    override fun generateRecipes(consumer: Consumer<FinishedRecipe>) {
        generateShapedRecipes(consumer)
        generateSmithingRecipes(consumer)
    }
}