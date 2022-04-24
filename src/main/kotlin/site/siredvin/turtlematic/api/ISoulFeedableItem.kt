package site.siredvin.turtlematic.api

import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface ISoulFeedableItem {
    fun consumeEntitySoul(stack: ItemStack, player: Player, entity: LivingEntity): InteractionResultHolder<ItemStack>
}