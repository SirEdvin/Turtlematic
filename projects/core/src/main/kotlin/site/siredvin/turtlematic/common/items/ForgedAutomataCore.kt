package site.siredvin.turtlematic.common.items

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.common.items.DescriptiveItem
import site.siredvin.peripheralium.util.Pair
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.api.RecipeEntityRepresentation
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry

class ForgedAutomataCore : DescriptiveItem(Properties().stacksTo(1).fireResistant()), ISoulFeedableItem {

    override fun consumeEntitySoul(
        stack: ItemStack,
        player: Player,
        entity: LivingEntity,
    ): Pair<ItemStack?, String?> {
        val recipe = SoulHarvestRecipeRegistry.searchRecipe(this, entity)
        if (recipe != null) {
            return recipe.consumeEntity(stack, entity)
        }
        return Pair.onlyRight("This item cannot hold soul of this entity")
    }

    override fun getEntityRepresentation(stack: ItemStack, recipe: SoulHarvestRecipe): List<RecipeEntityRepresentation> {
        val consumedData = stack.tag!!.getCompound(SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND)
        return recipe.ingredients.map {
            val entityData = consumedData.getCompound(it.name)
            return@map RecipeEntityRepresentation(
                entityData.getInt(SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT),
                it.requiredCount,
                it.description,
            )
        }
    }
}
