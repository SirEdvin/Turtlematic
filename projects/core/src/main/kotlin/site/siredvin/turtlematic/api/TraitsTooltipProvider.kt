package site.siredvin.turtlematic.api

import net.minecraft.network.chat.Component

fun interface TraitsTooltipProvider {
    fun addTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>)
}
