package site.siredvin.turtlematic.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks
import site.siredvin.broccolium.modules.data.recipe.TweakedShapedRecipeBuilder
import site.siredvin.broccolium.modules.data.recipe.TweakedSmithingTransformRecipeBuilder
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.xplat.ModRecipeIngredients
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider(output, registries) {
    fun generateShapedRecipes(consumer: RecipeOutput) {
        TweakedShapedRecipeBuilder(Items.SOUL_VIAL.get())
            .define('S', ModRecipeIngredients.get().soulLantern)
            .define('E', ModRecipeIngredients.get().emerald)
            .define('R', ModRecipeIngredients.get().redstoneDust)
            .pattern("R R")
            .pattern("ESE")
            .pattern("R R")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.AUTOMATA_CORE.get())
            .define('/', ModRecipeIngredients.get().stick)
            .define('S', Items.FILLED_SOUL_VIAL.get())
            .define('D', ModRecipeIngredients.get().diamond)
            .define('I', ModRecipeIngredients.get().ironIngot)
            .pattern("/I/")
            .pattern("DSD")
            .pattern("/I/")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.SOUL_SCRAPPER.get())
            .define('/', ModRecipeIngredients.get().stick)
            .define('S', ModRecipeIngredients.get().soulLantern)
            .pattern(" S ")
            .pattern("S/S")
            .pattern("/  ")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.TURTLE_CHATTER.get())
            .define('S', ModRecipeIngredients.get().computerSpeaker)
            .define('R', ModRecipeIngredients.get().redstoneDust)
            .define('I', ModRecipeIngredients.get().ironIngot)
            .pattern("RIR")
            .pattern("ISI")
            .pattern("RIR")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.MIMIC_GADGET.get())
            .define('S', ModRecipeIngredients.get().computerSpeaker)
            .define('R', ModRecipeIngredients.get().peripheralium)
            .define('I', ModRecipeIngredients.get().redstoneDust)
            .pattern("RIR")
            .pattern("ISI")
            .pattern("RIR")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.CHUNK_VIAL.get())
            .define('S', Items.FILLED_SOUL_VIAL.get())
            .define('G', ModRecipeIngredients.get().goldIngot)
            .define('D', ModRecipeIngredients.get().diamond)
            .define('E', ModRecipeIngredients.get().emerald)
            .pattern("GDG")
            .pattern("GSG")
            .pattern("GEG")
            .save(consumer)

        TweakedShapedRecipeBuilder(Items.INSPECTION_MONOCLE.get())
            .define('C', net.minecraft.world.item.Items.CHAIN)
            .define('G', Blocks.GLASS)
            .pattern(" C ")
            .pattern("CGC")
            .pattern(" C ")
            .save(consumer)
    }

    fun generateSmithingRecipes(consumer: RecipeOutput) {
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.FORGED_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.HUSBANDRY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.END_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_END_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.PROTECTIVE_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.NETHERITE_END_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_END_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE.get(),
        ).save(consumer)

        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.BREWING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_BREWING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.ENCHANTING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_ENCHANTING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.SMITHING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_SMITHING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.MASON_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_MASON_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedSmithingTransformRecipeBuilder(
            ModRecipeIngredients.get().peripheraliumUpgrade,
            Ingredient.of(Items.MERCANTILE_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_MERCANTILE_AUTOMATA_CORE.get(),
        ).save(consumer)
    }

    override fun buildRecipes(consumer: RecipeOutput) {
        generateShapedRecipes(consumer)
        generateSmithingRecipes(consumer)
    }
}
