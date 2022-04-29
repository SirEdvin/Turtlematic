package site.siredvin.lib.util

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.jvm.JvmOverloads

class LimitedInventory(private val parent: Container, private val usedSlots: IntArray) : Container {
    override fun getContainerSize(): Int {
        return usedSlots.size
    }

    override fun isEmpty(): Boolean {
        for (slot in usedSlots) {
            if (!parent.getItem(slot).isEmpty) return false
        }
        return true
    }

    override fun getItem(slot: Int): ItemStack {
        return parent.getItem(usedSlots[slot])
    }

    override fun removeItem(slot: Int, p_70298_2_: Int): ItemStack {
        return parent.removeItem(usedSlots[slot], p_70298_2_)
    }

    override fun removeItemNoUpdate(slot: Int): ItemStack {
        return parent.removeItemNoUpdate(usedSlots[slot])
    }

    override fun setItem(slot: Int, item: ItemStack) {
        parent.setItem(usedSlots[slot], item)
    }

    override fun setChanged() {
        parent.setChanged()
    }

    @JvmOverloads
    fun reduceCount(slot: Int, limit: Int = 1) {
        val item = parent.getItem(usedSlots[slot])
        val itemCount = item.count
        if (itemCount <= limit) {
            parent.setItem(usedSlots[slot], ItemStack.EMPTY)
        } else {
            item.count = item.count - limit
            parent.setItem(usedSlots[slot], item)
        }
    }

    override fun stillValid(player: Player): Boolean {
        return parent.stillValid(player)
    }

    override fun clearContent() {
        for (slot in usedSlots) parent.setItem(slot, ItemStack.EMPTY)
    }
}