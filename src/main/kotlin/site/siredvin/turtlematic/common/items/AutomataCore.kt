package site.siredvin.turtlematic.common.items

import dan200.computercraft.api.turtle.FakePlayer
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.lib.util.Pair
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.api.RecipeEntityRepresentation
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMED_ENTITY_NAME
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND
import site.siredvin.turtlematic.integrations.computercraft.turtle.Automata
import site.siredvin.turtlematic.util.text

class AutomataCore : BaseAutomataCore(AutomataCoreTier.TIER1, Automata.ID, {TurtlematicConfig.enableAutomataCore}), ISoulFeedableItem {

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        val record = getActiveRecipe(itemStack)
        if (record != null) {
            list.add(text("consumed_entities"))
            getEntityRepresentation(itemStack, record).forEach { list.add(it.toComponent()) }
        }
    }

    override fun consumeEntitySoul(
        stack: ItemStack,
        player: Player,
        entity: LivingEntity,
    ): Pair<ItemStack?, String?> {
        val entityType = EntityType.getKey(entity.type).toString()
        val recipe = SoulHarvestRecipeRegistry.searchRecipe(this, entityType)
        if (recipe != null) {
            val tag = stack.orCreateTag
            val consumedData = tag.getCompound(CONSUMER_ENTITY_COMPOUND)
            val correctedRecipe: SoulHarvestRecipe? = if (consumedData.isEmpty) {
                SoulHarvestRecipeRegistry.searchRecipe(this, entityType)
            } else {
                val anyKey = consumedData.allKeys.stream().findAny()
                if (!anyKey.isPresent)
                    return Pair.onlyRight("This item are corrupted by dark gods. I cannot be used for anything")
                SoulHarvestRecipeRegistry.searchRecipe(this, anyKey.get())
            }
            if (correctedRecipe == null || !correctedRecipe.isSuitable(entityType, consumedData))
                return Pair.onlyRight("This item cannot hold soul of this entity")
            entity.remove(Entity.RemovalReason.KILLED)
            val entityCompound = consumedData.getCompound(entityType)
            entityCompound.putInt(CONSUMED_ENTITY_COUNT, entityCompound.getInt(CONSUMED_ENTITY_COUNT) + 1)
            entityCompound.putString(CONSUMED_ENTITY_NAME, entity.name.string)
            consumedData.put(entityType, entityCompound)
            tag.put(CONSUMER_ENTITY_COMPOUND, consumedData)
            if (correctedRecipe.isFinished(consumedData)) {
                return Pair.onlyLeft(correctedRecipe.resultSoul.defaultInstance)
            }
            return Pair.onlyLeft(stack)
        }
        return Pair.onlyRight("This item cannot hold soul of this entity")
    }

    override fun getActiveRecipe(stack: ItemStack): SoulHarvestRecipe? {
        val tag: CompoundTag? = stack.tag
        if (tag != null && !tag.isEmpty) {
            val consumedData = tag.getCompound(CONSUMER_ENTITY_COMPOUND)
            return SoulHarvestRecipeRegistry.searchRecipe(this, consumedData.allKeys.first())
        }
        return null
    }

    override fun getEntityRepresentation(stack: ItemStack, recipe: SoulHarvestRecipe): List<RecipeEntityRepresentation> {
        val consumedData = stack.tag!!.getCompound(CONSUMER_ENTITY_COMPOUND)
        return recipe.getRequiredEntities().map { key: String ->
            val entityData = consumedData.getCompound(key)
            return@map RecipeEntityRepresentation(
                entityData.getInt(CONSUMED_ENTITY_COUNT),
                recipe.getRequiredCount(key),
                Registry.ENTITY_TYPE.get(ResourceLocation(key)).description.string
            )
        }
    }
}