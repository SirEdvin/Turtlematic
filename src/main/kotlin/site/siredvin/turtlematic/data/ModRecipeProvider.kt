package site.siredvin.turtlematic.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import site.siredvin.lib.data.TweakedShapedRecipeBuilder
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.tags.CommonItemTags
import java.util.function.Consumer

class ModRecipeProvider(dataGenerator: FabricDataGenerator) : FabricRecipeProvider(dataGenerator) {
    override fun generateRecipes(consumer: Consumer<FinishedRecipe>) {

        TweakedShapedRecipeBuilder.shaped(Items.SOUL_VIAL)
            .define('S', net.minecraft.world.item.Items.SOUL_LANTERN)
            .define('E', net.minecraft.world.item.Items.EMERALD)
            .define('R', net.minecraft.world.item.Items.REDSTONE)
            .pattern("R R")
            .pattern("ESE")
            .pattern("R R")
            .group(Turtlematic.MOD_ID)
            .save(consumer)

        TweakedShapedRecipeBuilder.shaped(Items.AUTOMATA_CORE)
            .define('/', net.minecraft.world.item.Items.STICK)
            .define('S', Items.FILLED_SOUL_VIAL)
            .define('D', net.minecraft.world.item.Items.DIAMOND)
            .define('I', net.minecraft.world.item.Items.IRON_INGOT)
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
    }
}