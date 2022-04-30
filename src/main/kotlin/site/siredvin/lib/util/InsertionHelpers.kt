package site.siredvin.lib.util

import dan200.computercraft.shared.util.InventoryUtil
import dan200.computercraft.shared.util.ItemStorage
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object InsertionHelpers {
    fun toInventoryOrToWorld(output: ItemStack, inventory: Container, startSlot: Int, outputPos: BlockPos, level: Level) {
        val rest =
            InventoryUtil.storeItems(output, ItemStorage.wrap(inventory), startSlot)
        if (!rest.isEmpty) {
            Containers.dropItemStack(level, outputPos.x.toDouble(), outputPos.y.toDouble(), outputPos.z.toDouble(), rest)
        }
    }
}