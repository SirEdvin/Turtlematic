package site.siredvin.turtlematic.common.recipe

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData

data class SoulHarvestRecipe(val ingredients: List<SoulHarvestIngredient>, val resultSoul: Item) {

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
            if (ingredient.name == name) {
                return true
            }
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
        return false
    }

    fun targetIngredient(entity: Entity): SoulHarvestIngredient? {
        for (ingredient in ingredients) {
            if (ingredient.match(entity)) {
                return ingredient
            }
        }
        return null
    }

    fun isFinished(consumedData: CompoundTag): Boolean = ingredients.stream().filter {
        it.requiredCount != consumedData.getCompound(it.name)
            .getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT)
    }.findAny().isEmpty

    fun consumeEntity(stack: ItemStack, entity: Entity): Pair<ItemStack?, String?> {
        val targetIngredient = targetIngredient(entity)
            ?: return Pair(null, "Cannot find ingredient that match this entity")
        entity.remove(Entity.RemovalReason.KILLED)
        val consumedData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()
        val entityCompound = consumedData.getCompound(targetIngredient.name)
        entityCompound.putInt(
            SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT,
            entityCompound.getInt(
                SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT,
            ) + 1,
        )
        entityCompound.putString(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_NAME, entity.name.string)
        consumedData.put(targetIngredient.name, entityCompound)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(consumedData))
        if (isFinished(consumedData)) {
            return Pair(resultSoul.defaultInstance, null)
        }
        return Pair(stack, null)
    }
}
