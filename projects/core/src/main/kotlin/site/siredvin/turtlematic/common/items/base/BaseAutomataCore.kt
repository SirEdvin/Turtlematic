package site.siredvin.turtlematic.common.items.base

import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.util.commonTooltips
import site.siredvin.turtlematic.util.isDisabled
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

open class BaseAutomataCore(
    val coreTier: IAutomataCoreTier,
    p: Properties,
    enableSup: Supplier<Boolean>,
    vararg tooltipHook: Function<PeripheralItem, List<Component>>,
    private val coreHook: BiFunction<ItemStack, Level?, List<Component>>? = null,
) : PeripheralItem(p, enableSup, alwaysShow = false, isDisabled, commonTooltips, *tooltipHook) {
    constructor(coreTier: IAutomataCoreTier, enableSup: Supplier<Boolean>, vararg tooltipHook: Function<PeripheralItem, List<Component>>, coreHook: BiFunction<ItemStack, Level?, List<Component>>? = null) : this(
        coreTier,
        Properties().stacksTo(1),
        enableSup,
        *tooltipHook,
        coreHook = coreHook,
    )

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        if (coreHook != null) {
            list.addAll(coreHook.apply(itemStack, level))
        }
        super.appendHoverText(itemStack, level, list, tooltipFlag)
    }
}
