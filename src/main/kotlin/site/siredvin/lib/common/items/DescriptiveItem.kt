package site.siredvin.lib.common.items

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.util.itemTooltip

open class DescriptiveItem(properties: Properties) : Item(properties) {
    constructor(): this(Properties().tab(Turtlematic.TAB))

    private var _description: TranslatableComponent? = null

    private val extraDescription: TranslatableComponent
        get() {
            if (_description == null)
                _description = itemTooltip(this.descriptionId)
            return _description!!
        }

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        if (extraDescription.key != extraDescription.string)
            list.add(extraDescription)
    }
}