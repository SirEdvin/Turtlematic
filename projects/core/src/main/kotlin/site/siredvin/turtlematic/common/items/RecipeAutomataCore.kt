package site.siredvin.turtlematic.common.items

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.data.ModTooltip
import java.util.function.Function
import java.util.function.Supplier

class RecipeAutomataCore(
    coreTier: IAutomataCoreTier,
    p: Properties,
    enableSup: Supplier<Boolean>,
    vararg tooltipHook: Function<PeripheralItem, List<Component>>,
) : BaseAutomataCore(coreTier, p, enableSup, *tooltipHook) {
    constructor(coreTier: IAutomataCoreTier, enableSup: Supplier<Boolean>, vararg tooltipHook: Function<PeripheralItem, List<Component>>) : this(
        coreTier,
        Properties().stacksTo(1),
        enableSup,
        *tooltipHook,
    )

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        if (InputConstants.isKeyDown(Minecraft.getInstance().window.window, InputConstants.KEY_LCONTROL)) {
            val recipe = SoulHarvestRecipeRegistry.get(this)
            if (recipe == null) {
                list.add(ModTooltip.RECIPE_MISSING.text)
            } else {
                list.add(ModTooltip.SOUL_UPGRADE_FROM.format(recipe.second.description.string))
                list.add(ModTooltip.REQUIRED_SOULS.format(recipe.first.ingredients.joinToString { it.description }))
            }
        } else {
            list.add(ModTooltip.PRESS_FOR_RECIPE.text)
        }
    }
}
