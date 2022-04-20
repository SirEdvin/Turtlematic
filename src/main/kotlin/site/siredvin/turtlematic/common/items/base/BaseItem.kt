package site.siredvin.turtlematic.common.items.base

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.util.itemTooltip


abstract class BaseItem(properties: Item.Settings): Item(properties) {
    private var description: Text? = null

    constructor(): this(Settings().group(Turtlematic.TAB))

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        tooltip?.add(getDescription())
    }

    open fun getDescription(): Text {
        println(translationKey)
        if (description == null) description = itemTooltip(translationKey)
        return description!!
    }

    abstract fun isEnabled(): Boolean
}