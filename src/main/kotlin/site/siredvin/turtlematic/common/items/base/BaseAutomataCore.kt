package site.siredvin.turtlematic.common.items.base

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.lib.api.TurtleIDBuildFunction
import site.siredvin.lib.common.items.TurtleItem
import site.siredvin.lib.util.text
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.TraitsTooltipProvider
import site.siredvin.turtlematic.util.TooltipHandlerCollection
import java.util.function.Supplier

open class BaseAutomataCore(
    val coreTier: IAutomataCoreTier, p: Properties, enableSup: Supplier<Boolean>, turtleIDSup: TurtleIDBuildFunction = TurtleIDBuildFunction.WITHOUT_CORE
) :
    TurtleItem(p, enableSup, turtleIDSup) {
    constructor(coreTier: IAutomataCoreTier, enableSup: Supplier<Boolean>, turtleIDSup: TurtleIDBuildFunction = TurtleIDBuildFunction.WITHOUT_CORE): this(
        coreTier, Properties().tab(Turtlematic.TAB).stacksTo(1), enableSup, turtleIDSup
    )

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        if (InputConstants.isKeyDown(Minecraft.getInstance().window.window, InputConstants.KEY_LSHIFT)) {
            list.add(text(Turtlematic.MOD_ID, "core_configuration"))
            list.add(text(Turtlematic.MOD_ID, "interaction_radius", coreTier.interactionRadius))
            list.add(text(Turtlematic.MOD_ID, "max_fuel_consumption_rate", coreTier.maxFuelConsumptionRate))
            if (coreTier.cooldownReduceFactor != 1.0)
                list.add(text(Turtlematic.MOD_ID, "cooldown_reduce_factor", coreTier.cooldownReduceFactor))
            if (coreTier.traits.isNotEmpty())
                TooltipHandlerCollection.getProvidersForItem(this)?.forEach{ it.addTooltip(coreTier, list) }
        } else {
            list.add(text(Turtlematic.MOD_ID, "press_for_description"))
        }
    }
}