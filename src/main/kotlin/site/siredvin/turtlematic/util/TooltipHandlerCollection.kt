package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.item.Item
import site.siredvin.lib.util.text
import site.siredvin.turtlematic.Turtlematic
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
            tooltipList.add(text(Turtlematic.MOD_ID, "durability_refund_chance"))
    }

    fun durabilityRefundTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND))
            tooltipList.add(text(Turtlematic.MOD_ID, "durability_refund"))
    }

    fun registerDefaults() {
        registerProvider(Items.NETHERITE_END_AUTOMATA_CORE, this::durabilityRefundChanceTooltip)
        registerProvider(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE, this::durabilityRefundChanceTooltip)
        registerProvider(Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE, this::durabilityRefundTooltip)
        registerProvider(Items.STARBOUND_END_AUTOMATA_CORE, this::durabilityRefundTooltip)
        registerProvider(Items.ENORMOUS_AUTOMATA_CORE, this::durabilityRefundTooltip)
    }
}