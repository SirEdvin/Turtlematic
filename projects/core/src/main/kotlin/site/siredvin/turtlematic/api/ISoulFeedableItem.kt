package site.siredvin.turtlematic.api

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry

interface ISoulFeedableItem : ItemLike {
    fun consumeEntitySoul(stack: ItemStack, player: Player, entity: LivingEntity): Pair<ItemStack?, String?>
    fun getActiveRecipe(stack: ItemStack): SoulHarvestRecipe? {
        val tag: CompoundTag? = stack.get(DataComponents.CUSTOM_DATA)?.copyTag()
        if (tag != null && !tag.isEmpty) {
            val consumedData = tag.getCompound(SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND)
            if (!consumedData.isEmpty) {
                val recipeName = consumedData.allKeys.firstOrNull() ?: return null
                return SoulHarvestRecipeRegistry.searchRecipe(this.asItem(), recipeName)
            }
        }
        return null
    }
    fun getEntityRepresentation(stack: ItemStack, recipe: SoulHarvestRecipe): List<RecipeEntityRepresentation>
}
