package site.siredvin.turtlematic.common.items.base

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import site.siredvin.lib.common.items.TurtleItem
import site.siredvin.lib.util.text
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.api.IAutomataCoreTier
import java.util.function.Supplier

open class BaseAutomataCore(val coreTier: IAutomataCoreTier, p: Properties, turtleID: ResourceLocation, enableSup: Supplier<Boolean>) :
    TurtleItem(p, turtleID, enableSup) {
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
        if (InputConstants.isKeyDown(Minecraft.getInstance().window.window, InputConstants.KEY_LSHIFT)) {
            list.add(text(Turtlematic.MOD_ID, "core_configuration"))
            list.add(text(Turtlematic.MOD_ID, "interaction_radius", coreTier.interactionRadius))
            list.add(text(Turtlematic.MOD_ID, "max_fuel_consumption_rate", coreTier.maxFuelConsumptionRate))
        } else {
            list.add(text(Turtlematic.MOD_ID, "press_for_description"))
        }
    }
}