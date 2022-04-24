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
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.TurtleItem
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipe
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMED_ENTITY_COUNT
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMED_ENTITY_NAME
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND
import site.siredvin.turtlematic.integrations.computercraft.turtle.Automata
import site.siredvin.turtlematic.util.text

class AutomataCore : TurtleItem(Automata.ID, {TurtlematicConfig.enableAutomataCore}), ISoulFeedableItem {

    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, level, list, tooltipFlag)
        val tag: CompoundTag? = stack.tag
        if (tag != null && !tag.isEmpty) {
            val consumedData = tag.getCompound(CONSUMER_ENTITY_COMPOUND)
            val record: SoulHarvestRecipe? = SoulHarvestRecipeRegistry.searchRecipe(this, consumedData.allKeys.first())
            if (record != null)
                list.add(text("consumed_entities"))
            record?.getRequiredEntities()?.forEach { key: String ->
                val entityData = consumedData.getCompound(key)
                list.add(
                    TextComponent("  %s/%s of %s".format(
                        entityData.getInt(CONSUMED_ENTITY_COUNT),
                        record.getRequiredCount(key),
                        Registry.ENTITY_TYPE.get(ResourceLocation(key)).description.string
                    ))
                )
            }
        }
    }

    override fun consumeEntitySoul(
        stack: ItemStack,
        player: Player,
        entity: LivingEntity,
    ): InteractionResultHolder<ItemStack> {
        if (player !is FakePlayer) {
            player.displayClientMessage(
                text("automata_core_feed_by_player"),
                true
            )
            return InteractionResultHolder.fail(stack)
        }
        val entityType = EntityType.getKey(entity.type).toString()
        val recipe = SoulHarvestRecipeRegistry.searchRecipe(this, entityType)
        if (recipe != null) {
            val tag = stack.orCreateTag
            val consumedData = tag.getCompound(CONSUMER_ENTITY_COMPOUND)
            val correctedRecipe: SoulHarvestRecipe? = if (consumedData.isEmpty) {
                SoulHarvestRecipeRegistry.searchRecipe(this, entityType)
            } else {
                val anyKey = consumedData.allKeys.stream().findAny()
                if (!anyKey.isPresent) return InteractionResultHolder.pass(stack)
                SoulHarvestRecipeRegistry.searchRecipe(this, anyKey.get())
            }
            if (correctedRecipe == null || !correctedRecipe.isSuitable(entityType, consumedData))
                return InteractionResultHolder.pass(stack)
            entity.remove(Entity.RemovalReason.KILLED)
            val entityCompound = consumedData.getCompound(entityType)
            entityCompound.putInt(CONSUMED_ENTITY_COUNT, entityCompound.getInt(CONSUMED_ENTITY_COUNT) + 1)
            entityCompound.putString(CONSUMED_ENTITY_NAME, entity.name.string)
            consumedData.put(entityType, entityCompound)
            tag.put(CONSUMER_ENTITY_COMPOUND, consumedData)
            if (correctedRecipe.isFinished(consumedData)) {
                return InteractionResultHolder.success(correctedRecipe.resultSoul.defaultInstance)
            }
            return InteractionResultHolder.success(stack)
        }
        return InteractionResultHolder.pass(stack)
    }
}