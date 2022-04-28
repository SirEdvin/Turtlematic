package site.siredvin.turtlematic.api

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.util.Pair
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe

interface ISoulFeedableItem {
    fun consumeEntitySoul(stack: ItemStack, player: Player, entity: LivingEntity): Pair<ItemStack?, String?>
    fun getActiveRecipe(stack: ItemStack): SoulHarvestRecipe?
    fun getEntityRepresentation(stack: ItemStack, recipe: SoulHarvestRecipe): List<RecipeEntityRepresentation>
}