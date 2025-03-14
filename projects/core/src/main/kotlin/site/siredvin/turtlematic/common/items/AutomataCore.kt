package site.siredvin.turtlematic.common.items

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.api.RecipeEntityRepresentation
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT
import site.siredvin.turtlematic.data.ModTooltip

class AutomataCore :
    BaseAutomataCore(AutomataCoreTier.TIER1, { TurtlematicConfig.enableAutomataCore }),
    ISoulFeedableItem {

    override fun appendHoverText(
        itemStack: ItemStack,
        context: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(itemStack, context, list, tooltipFlag)
        val record = getActiveRecipe(itemStack)
        if (record != null) {
            list.add(ModTooltip.CONSUMED_ENTITIES.text)
            getEntityRepresentation(itemStack, record).forEach { list.add(it.toComponent()) }
        }
    }

    override fun consumeEntitySoul(
        stack: ItemStack,
        player: Player,
        entity: LivingEntity,
    ): Pair<ItemStack?, String?> {
        val recipe = SoulHarvestRecipeRegistry.searchRecipe(this, entity)
        if (recipe != null) {
            val consumedData = stack.get(DataComponents.CUSTOM_DATA)!!.copyTag()
            val correctedRecipe: SoulHarvestRecipe? = if (consumedData.isEmpty) {
                SoulHarvestRecipeRegistry.searchRecipe(this, entity)
            } else {
                val anyKey = consumedData.allKeys.stream().findAny()
                if (!anyKey.isPresent) {
                    return Pair(null, "This item are corrupted by dark gods. I cannot be used for anything")
                }
                SoulHarvestRecipeRegistry.searchRecipe(this, anyKey.get())
            }
            if (correctedRecipe == null || !correctedRecipe.isSuitable(entity, consumedData)) {
                return Pair(null, "This item cannot hold soul of this entity")
            }
            return correctedRecipe.consumeEntity(stack, entity)
        }
        return Pair(null, "This item cannot hold soul of this entity")
    }

    override fun getEntityRepresentation(stack: ItemStack, recipe: SoulHarvestRecipe): List<RecipeEntityRepresentation> {
        val consumedData = stack.get(DataComponents.CUSTOM_DATA)!!.copyTag()
        return recipe.ingredients.map {
            val entityData = consumedData.getCompound(it.name)
            return@map RecipeEntityRepresentation(
                entityData.getInt(CONSUMED_ENTITY_COUNT),
                it.requiredCount,
                it.description,
            )
        }
    }
}
