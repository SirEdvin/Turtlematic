package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.TraitsTooltipProvider
import site.siredvin.turtlematic.common.setup.Items

object TooltipHandlerCollection {

    val TOOLTIP_PROVIDERS_PER_ITEM: MutableMap<Item, MutableList<TraitsTooltipProvider>> = hashMapOf()

    fun registerProvider(item: Item, vararg provider: TraitsTooltipProvider) {
        if (!TOOLTIP_PROVIDERS_PER_ITEM.contains(item))
            TOOLTIP_PROVIDERS_PER_ITEM[item] = mutableListOf()
        TOOLTIP_PROVIDERS_PER_ITEM[item]!!.addAll(provider)
    }

    fun getProvidersForItem(item: Item): List<TraitsTooltipProvider>? {
        return TOOLTIP_PROVIDERS_PER_ITEM[item]
    }

    fun durabilityRefundChanceTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE))
            tooltipList.add(TextComponent("Item has chance to not loose durability on use"))
    }

    fun durabilityRefundTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE))
            tooltipList.add(TextComponent("Item will not loose durability on use"))
    }

    fun registerDefaults() {
        registerProvider(Items.AUTOMATA_CORE, this::durabilityRefundChanceTooltip)
        registerProvider(Items.ENORMOUS_AUTOMATA_CORE, this::durabilityRefundTooltip)
    }
}