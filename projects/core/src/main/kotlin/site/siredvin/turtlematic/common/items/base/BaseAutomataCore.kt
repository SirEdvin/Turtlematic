package site.siredvin.turtlematic.common.items.base

import net.minecraft.network.chat.Component
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.util.commonTooltips
import site.siredvin.turtlematic.util.isDisabled
import java.util.function.Function
import java.util.function.Supplier

open class BaseAutomataCore(
    val coreTier: IAutomataCoreTier, p: Properties, enableSup: Supplier<Boolean>, vararg tooltipHook: Function<PeripheralItem, List<Component>>
) : PeripheralItem(p, enableSup, alwaysShow = false, isDisabled, commonTooltips, *tooltipHook) {
    constructor(coreTier: IAutomataCoreTier, enableSup: Supplier<Boolean>, vararg tooltipHook: Function<PeripheralItem, List<Component>>): this(
        coreTier, Properties().stacksTo(1), enableSup, *tooltipHook
    )
}