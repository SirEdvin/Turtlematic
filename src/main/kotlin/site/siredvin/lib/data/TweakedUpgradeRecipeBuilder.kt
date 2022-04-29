package site.siredvin.lib.data

import com.google.gson.JsonObject
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.advancements.RequirementsStrategy
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.Registry
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.UpgradeRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class TweakedUpgradeRecipeBuilder(
    private val type: RecipeSerializer<*>,
    private val base: Ingredient,
    private val addition: Ingredient,
    private val result: Item
) {

    companion object {
        fun smithing(ingredient: Ingredient, ingredient2: Ingredient, item: Item): TweakedUpgradeRecipeBuilder {
            return TweakedUpgradeRecipeBuilder(RecipeSerializer.SMITHING, ingredient, ingredient2, item)
        }
    }

    fun save(consumer: Consumer<FinishedRecipe>) {
        this.save(consumer, Registry.ITEM.getKey(result))
    }

    fun save(consumer: Consumer<FinishedRecipe>, resourceLocation: ResourceLocation) {
        val var10004 = type
        val var10005 = base
        val var10006 = addition
        val var10007 = result
        consumer.accept(
            Result(
                resourceLocation,
                var10004,
                var10005,
                var10006,
                var10007
            )
        )
    }

    class Result(
        private val id: ResourceLocation,
        private val type: RecipeSerializer<*>,
        private val base: Ingredient,
        private val addition: Ingredient,
        private val result: Item,
    ) :
        FinishedRecipe {
        override fun serializeRecipeData(jsonObject: JsonObject) {
            jsonObject.add("base", base.toJson())
            jsonObject.add("addition", addition.toJson())
            val jsonObject2 = JsonObject()
            jsonObject2.addProperty("item", Registry.ITEM.getKey(result).toString())
            jsonObject.add("result", jsonObject2)
        }

        override fun getId(): ResourceLocation {
            return id
        }

        override fun getType(): RecipeSerializer<*> {
            return type
        }

        override fun serializeAdvancement(): JsonObject? {
            return null
        }

        override fun getAdvancementId(): ResourceLocation? {
            return null
        }
    }
}