package site.siredvin.lib.util

import dan200.computercraft.api.ComputerCraftAPI
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object ItemUtil {
    private val POCKET_NORMAL: Item
        get() = Registry.ITEM.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "pocket_computer_normal"))

    private val POCKET_ADVANCED: Item
        get() = Registry.ITEM.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "pocket_computer_advanced"))

    private val TURTLE_NORMAL: Item
        get() = Registry.ITEM.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "turtle_normal"))

    private val TURTLE_ADVANCED: Item
        get() = Registry.ITEM.get(ResourceLocation(ComputerCraftAPI.MOD_ID, "turtle_advanced"))

    fun makeTurtle(upgrade: String): ItemStack {
        val stack = ItemStack(TURTLE_NORMAL)
        stack.orCreateTag.putString("RightUpgrade", upgrade)
        return stack
    }

    fun makeAdvancedTurtle(upgrade: String): ItemStack {
        val stack = ItemStack(TURTLE_ADVANCED)
        stack.orCreateTag.putString("RightUpgrade", upgrade)
        return stack
    }

    fun makePocket(upgrade: String): ItemStack {
        val stack = ItemStack(POCKET_NORMAL)
        stack.orCreateTag.putString("Upgrade", upgrade)
        return stack
    }

    fun makeAdvancedPocket(upgrade: String): ItemStack {
        val stack = ItemStack(POCKET_ADVANCED)
        stack.orCreateTag.putString("Upgrade", upgrade)
        return stack
    }
}