package site.siredvin.turtlematic.data

import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.peripheralium.data.blocks.TweakedShapedRecipeBuilder
import site.siredvin.peripheralium.data.blocks.TweakedUpgradeRecipeBuilder
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.xplat.ModRecipeIngredients
import java.util.*
import java.util.function.Consumer

class ModRecipeProvider(output: PackOutput) : RecipeProvider(output) {
    fun generateShapedRecipes(consumer: Consumer<FinishedRecipe>) {
        TweakedShapedRecipeBuilder.shaped(Items.SOUL_VIAL.get())
            .define('S', ModRecipeIngredients.get().soulLantern)
            .define('E', ModRecipeIngredients.get().emerald)
            .define('R', ModRecipeIngredients.get().redstoneDust)
            .pattern("R R")
            .pattern("ESE")
            .pattern("R R")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.AUTOMATA_CORE.get())
            .define('/', ModRecipeIngredients.get().stick)
            .define('S', Items.FILLED_SOUL_VIAL.get())
            .define('D', ModRecipeIngredients.get().diamond)
            .define('I', ModRecipeIngredients.get().ironIngot)
            .pattern("/I/")
            .pattern("DSD")
            .pattern("/I/")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.SOUL_SCRAPPER.get())
            .define('/', ModRecipeIngredients.get().stick)
            .define('S', ModRecipeIngredients.get().soulLantern)
            .pattern(" S ")
            .pattern("S/S")
            .pattern("/  ")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.TURTLE_CHATTER.get())
            .define('S', ModRecipeIngredients.get().computerSpeaker)
            .define('R', ModRecipeIngredients.get().redstoneDust)
            .define('I', ModRecipeIngredients.get().ironIngot)
            .pattern("RIR")
            .pattern("ISI")
            .pattern("RIR")
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.CHUNK_VIAL.get())
            .define('S', Items.FILLED_SOUL_VIAL.get())
            .define('G', ModRecipeIngredients.get().goldIngot)
            .define('D', ModRecipeIngredients.get().diamond)
            .define('E', ModRecipeIngredients.get().emerald)
            .pattern("GDG")
            .pattern("GSG")
            .pattern("GEG")
            .save(consumer)
    }

    fun generateSmithingRecipes(consumer: Consumer<FinishedRecipe>) {
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.FORGED_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.HUSBANDRY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.FLUIDY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_FLUIDY_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.END_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netheriteIngot,
            Items.NETHERITE_END_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_END_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_END_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.NETHERITE_FLUIDY_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_FLUIDY_AUTOMATA_CORE.get(),
        ).save(consumer)

        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.BREWING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_BREWING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.ENCHANTING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_ENCHANTING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.SMITHING_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_SMITHING_AUTOMATA_CORE.get(),
        ).save(consumer)
        TweakedUpgradeRecipeBuilder.smithing(
            Ingredient.of(Items.MASON_AUTOMATA_CORE.get()),
            ModRecipeIngredients.get().netherStar,
            Items.STARBOUND_MASON_AUTOMATA_CORE.get(),
        ).save(consumer)
    }

    override fun buildRecipes(consumer: Consumer<FinishedRecipe>) {
        generateShapedRecipes(consumer)
        generateSmithingRecipes(consumer)
    }
}
