package site.siredvin.turtlematic.common.recipe

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.util.Pair
import java.util.function.Predicate

data class SoulHarvestRecipe(val ingredients: List<SoulHarvestIngredient>, val resultSoul: Item) {
//    fun getRequiredCount(entityType: String): Int {
//        return this.ingredients.getOrDefault(entityType, 0)
//    }
//
//    fun getRequiredEntities(): Set<String> {
//        return ingredients.keys
//    }

    fun isSuitable(entity: Entity): Boolean {
        for (ingredient in ingredients) {
            if (ingredient.match(entity)) {
               return true
            }
        }
        return false
    }

    fun isSuitable(name: String): Boolean {
        for (ingredient in ingredients) {
            if (ingredient.name == name)
                return true
        }
        return false
    }

    fun isSuitable(entity: Entity, consumedData: CompoundTag): Boolean {
        for (ingredient in ingredients) {
            if (ingredient.match(entity)) {
                val currentCount = consumedData.getCompound(ingredient.name).getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT)
                return currentCount < ingredient.requiredCount
            }
        }
        return false;
    }

    fun targetIngredient(entity: Entity): SoulHarvestIngredient? {
        for (ingredient in ingredients) {
            if (ingredient.match(entity)) {
                return ingredient
            }
        }
        return null
    }

    fun isFinished(consumedData: CompoundTag): Boolean {
        return ingredients.stream().filter {
            it.requiredCount != consumedData.getCompound(it.name)
                .getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT)
        }.findAny().isEmpty
    }

    fun consumeEntity(stack: ItemStack, entity: Entity): Pair<ItemStack?, String?> {
        val targetIngredient = targetIngredient(entity)
            ?: return Pair.onlyRight("Cannot find ingredient that match this entity")
        entity.remove(Entity.RemovalReason.KILLED)
        val tag = stack.orCreateTag
        val consumedData = tag.getCompound(SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND)
        val entityCompound = consumedData.getCompound(targetIngredient.name)
        entityCompound.putInt(
            SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT, entityCompound.getInt(
                SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT
            ) + 1)
        entityCompound.putString(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_NAME, entity.name.string)
        consumedData.put(targetIngredient.name, entityCompound)
        tag.put(SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND, consumedData)
        if (isFinished(consumedData)) {
            return Pair.onlyLeft(resultSoul.defaultInstance)
        }
        return Pair.onlyLeft(stack)
    }
}