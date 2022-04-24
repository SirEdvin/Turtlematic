package site.siredvin.turtlematic.common.recipe

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.Item

data class SoulHarvestRecipe(val ingredients: Map<String, Int>, val resultSoul: Item) {
    fun getRequiredCount(entityType: String): Int {
        return this.ingredients.getOrDefault(entityType, 0)
    }

    fun getRequiredEntities(): Set<String> {
        return ingredients.keys
    }

    fun isSuitable(entityType: String, consumedData: CompoundTag): Boolean {
        if (!ingredients.containsKey(entityType))
            return false
        val requiredCount: Int = ingredients[entityType]!!
        val currentCount = consumedData.getCompound(entityType)
            .getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT)
        return currentCount < requiredCount
    }

    fun isFinished(consumedData: CompoundTag): Boolean {
        return ingredients.entries.stream().map { (key, value): Map.Entry<String, Int> ->
            value == consumedData.getCompound(key).getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT)
        }.reduce { a: Boolean, b: Boolean -> a && b }.orElse(true)
    }
}