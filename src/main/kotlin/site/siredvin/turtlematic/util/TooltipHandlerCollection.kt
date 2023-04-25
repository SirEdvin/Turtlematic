package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import site.siredvin.peripheralium.util.text
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.api.TraitsTooltipProvider
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
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

    fun commonTooltips(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (coreTier.traits.contains(AutomataCoreTraits.STARBOUND_REGENERATION))
            tooltipList.add(text(Turtlematic.MOD_ID, "starbound_generation"))
        if (coreTier.traits.contains(AutomataCoreTraits.FUEL_CONSUMPTION_DISABLED))
            tooltipList.add(text(Turtlematic.MOD_ID, "fuel_consumption_disabled"))
    }

    fun interactionAPITooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE))
            tooltipList.add(text(Turtlematic.MOD_ID, "durability_refund_chance"))
        if (coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND))
            tooltipList.add(text(Turtlematic.MOD_ID, "durability_refund"))
    }

    fun enchantingTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (!coreTier.traits.contains(AutomataCoreTraits.SKILLED))
            tooltipList.add(text(Turtlematic.MOD_ID, "enchantment_wipe_chance", (TurtlematicConfig.enchantmentWipeChance * 100).toInt()))
        if (coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
            tooltipList.add(text(Turtlematic.MOD_ID, "enchantment_no_wipe"))
            tooltipList.add(text(Turtlematic.MOD_ID, "enchantment_treasure_allowed"))
        }
    }

    fun husbandryTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        if (TurtlematicConfig.husbandryAutomataRandomTicksEnabled) {
            if (coreTier.traits.contains(AutomataCoreTraits.MASTERPIECE))
                tooltipList.add(text(Turtlematic.MOD_ID, "periodical_area_grown_accelerator"))
            else if (coreTier.traits.contains(AutomataCoreTraits.APPRENTICE))
                tooltipList.add(text(Turtlematic.MOD_ID, "periodical_single_grown_accelerator"))
        }
    }

    fun tradingTooltip(coreTier: IAutomataCoreTier, tooltipList: MutableList<Component>) {
        tooltipList.add(text(Turtlematic.MOD_ID, "has_trade_abilities"))
        if (coreTier.traits.contains(AutomataCoreTraits.SKILLED))
            tooltipList.add(text(Turtlematic.MOD_ID, "can_restock_traders"))
    }

    fun registerDefaults() {
        registerProvider(Items.NETHERITE_END_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
        registerProvider(Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip, this::husbandryTooltip)

        registerProvider(Items.MASON_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.SMITHING_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.BREWING_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
        registerProvider(Items.ENCHANTING_AUTOMATA_CORE, this::commonTooltips, this::enchantingTooltip)

        registerProvider(Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip, this::husbandryTooltip)
        registerProvider(Items.STARBOUND_MERCANTILE_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip, this::tradingTooltip)
        registerProvider(Items.STARBOUND_END_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)

        registerProvider(Items.STARBOUND_MASON_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.STARBOUND_SMITHING_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.STARBOUND_BREWING_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
        registerProvider(Items.STARBOUND_ENCHANTING_AUTOMATA_CORE, this::commonTooltips, this::enchantingTooltip)

        registerProvider(Items.CREATIVE_END_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
        registerProvider(Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip, this::husbandryTooltip)
        registerProvider(Items.CREATIVE_MERCANTILE_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip, this::tradingTooltip)

        registerProvider(Items.CREATIVE_MASON_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.CREATIVE_SMITHING_AUTOMATA_CORE, this::commonTooltips)
        registerProvider(Items.CREATIVE_BREWING_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
        registerProvider(Items.CREATIVE_ENCHANTING_AUTOMATA_CORE, this::commonTooltips, this::enchantingTooltip)

        registerProvider(Items.ENORMOUS_AUTOMATA_CORE, this::commonTooltips, this::interactionAPITooltip)
    }
}