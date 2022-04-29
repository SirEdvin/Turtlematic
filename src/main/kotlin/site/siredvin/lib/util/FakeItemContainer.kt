package site.siredvin.lib.util

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class FakeItemContainer(private var stack: ItemStack): Container {
    override fun clearContent() {
        stack = ItemStack.EMPTY
    }

    override fun getContainerSize(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return stack.isEmpty
    }

    override fun getItem(i: Int): ItemStack {
        return stack
    }

    override fun removeItem(i: Int, j: Int): ItemStack {
        throw IllegalArgumentException("Should be called")
    }

    override fun removeItemNoUpdate(i: Int): ItemStack {
        throw IllegalArgumentException("Should be called")
    }

    override fun setItem(i: Int, itemStack: ItemStack) {
        throw IllegalArgumentException("Should be called")
    }

    override fun setChanged() {
    }

    override fun stillValid(player: Player): Boolean {
        return true
    }
}