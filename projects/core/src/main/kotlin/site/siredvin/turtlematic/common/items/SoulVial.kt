package site.siredvin.turtlematic.common.items

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.CustomModelData
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.HitResult
import site.siredvin.broccolium.modules.base.item.DescriptiveItem
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.data.ModTooltip

class SoulVial : DescriptiveItem(Properties().stacksTo(1).fireResistant()) {

    companion object {
        private const val SOUL_LIMIT = 4
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        context: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(itemStack, context, list, tooltipFlag)
        val data = itemStack.get(DataComponents.CUSTOM_MODEL_DATA)
        if (data != null) {
            list.add(ModTooltip.SOUL_VIAL_PROGRESS.format(data.value, SOUL_LIMIT))
        }
    }

    override fun use(level: Level, player: Player, interactionHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = player.getItemInHand(interactionHand)
        val blockHitResult = getPlayerPOVHitResult(
            level,
            player,
            ClipContext.Fluid.NONE,
        )
        return if (blockHitResult.type == HitResult.Type.MISS) {
            InteractionResultHolder.pass(itemStack)
        } else if (blockHitResult.type != HitResult.Type.BLOCK) {
            InteractionResultHolder.pass(itemStack)
        } else {
            val blockPos = blockHitResult.blockPos
            val direction = blockHitResult.direction
            val blockPos2 = blockPos.relative(direction)
            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos2, direction, itemStack)) {
                val blockState = level.getBlockState(blockPos)
                if (blockState.`is`(Blocks.SOUL_SAND)) {
                    val fillLevel = itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value + 1
                    level.setBlockAndUpdate(blockPos, Blocks.SAND.defaultBlockState())
                    return if (fillLevel < SOUL_LIMIT) {
                        itemStack.set(DataComponents.CUSTOM_MODEL_DATA, CustomModelData(fillLevel))
                        InteractionResultHolder.success(itemStack)
                    } else {
                        InteractionResultHolder.consume(Items.FILLED_SOUL_VIAL.get().defaultInstance)
                    }
                }
                return InteractionResultHolder.pass(itemStack)
            } else {
                InteractionResultHolder.fail(itemStack)
            }
        }
    }
}
