package site.siredvin.turtlematic.common.items

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.util.text
import java.util.function.Supplier

class RecipeAutomataCore(
    coreTier: IAutomataCoreTier,
    p: Properties,
    turtleID: ResourceLocation,
    enableSup: Supplier<Boolean>
) : BaseAutomataCore(coreTier, p, turtleID, enableSup) {
    constructor(coreTier: IAutomataCoreTier, turtleID: ResourceLocation, enableSup: Supplier<Boolean>): this(
        coreTier, Properties().tab(Turtlematic.TAB).stacksTo(1), turtleID, enableSup
    )

    override fun appendHoverText(
        itemStack: ItemStack,
        level: Level?,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, level, list, tooltipFlag)
        if (InputConstants.isKeyDown(Minecraft.getInstance().window.window, InputConstants.KEY_LCONTROL)) {
            val recipe = SoulHarvestRecipeRegistry.get(this)
            if (recipe == null) {
                list.add(text("recipe_missing"))
            } else {
                list.add(text("soul_upgrade_from", recipe.second.description.string))
                list.add(
                    text(
                        "required_souls_for_consuming",
                        recipe.first.ingredients.joinToString { it.description }
                    )
                )
            }
        } else {
            list.add(text("press_for_recipe"))
        }
    }
}