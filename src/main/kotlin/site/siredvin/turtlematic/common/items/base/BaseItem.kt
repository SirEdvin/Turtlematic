package site.siredvin.turtlematic.common.items.base

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.util.itemTooltip


abstract class BaseItem(properties: Properties): Item(properties) {
    private var _description: Component? = null

    private val extraDescription: Component
        get() {
            if (_description == null)
                _description = itemTooltip(this.descriptionId)
            return _description!!
        }

    constructor(): this(Properties().tab(Turtlematic.TAB))

    abstract fun isEnabled(): Boolean

//    override fun appendHoverText(
//        itemStack: ItemStack,
//        level: Level,
//        list: MutableList<Component>,
//        tooltipFlag: TooltipFlag
//    ) {
//        super.appendHoverText(itemStack, level, list, tooltipFlag)
//        list.add(extraDescription)
//    }
}