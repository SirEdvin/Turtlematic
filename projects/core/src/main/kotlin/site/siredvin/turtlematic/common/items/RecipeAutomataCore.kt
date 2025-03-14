package site.siredvin.turtlematic.common.items

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import site.siredvin.broccolium.modules.base.item.HiddenDescriptiveItemItem
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.data.ModTooltip
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Supplier

class RecipeAutomataCore(
    coreTier: IAutomataCoreTier,
    p: Properties,
    enableSup: Supplier<Boolean>,
    vararg tooltipHook: Function<HiddenDescriptiveItemItem, List<Component>>,
    coreHook: BiFunction<ItemStack, TooltipContext, List<Component>>? = null,
) : BaseAutomataCore(coreTier, p, enableSup, *tooltipHook, coreHook = coreHook) {
    constructor(coreTier: IAutomataCoreTier, enableSup: Supplier<Boolean>, vararg tooltipHook: Function<HiddenDescriptiveItemItem, List<Component>>, coreHook: BiFunction<ItemStack, TooltipContext, List<Component>>? = null) : this(
        coreTier,
        Properties().stacksTo(1),
        enableSup,
        *tooltipHook,
        coreHook = coreHook,
    )

    override fun appendHoverText(
        itemStack: ItemStack,
        context: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag,
    ) {
        super.appendHoverText(itemStack, context, list, tooltipFlag)
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
