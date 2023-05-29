package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.peripheralium.util.text
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import java.util.function.Function

val isDisabled = Function<PeripheralItem, List<Component>> { item ->
    if (!item.isEnabled()) {
        return@Function listOf(text(TurtlematicCore.MOD_ID, "item_disabled"))
    }
    return@Function emptyList()
}

val commonTooltips = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    tooltipList.add(text(TurtlematicCore.MOD_ID, "core_configuration"))
    tooltipList.add(text(TurtlematicCore.MOD_ID, "interaction_radius", item.coreTier.interactionRadius))
    tooltipList.add(text(TurtlematicCore.MOD_ID, "max_fuel_consumption_rate", item.coreTier.maxFuelConsumptionRate))
    if (item.coreTier.cooldownReduceFactor != 1.0) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "cooldown_reduce_factor", item.coreTier.cooldownReduceFactor))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.STARBOUND_REGENERATION)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "starbound_generation"))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.FUEL_CONSUMPTION_DISABLED)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "fuel_consumption_disabled"))
    }
    return@Function tooltipList
}

val itemUsageTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "durability_refund_chance"))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "durability_refund"))
    }
    return@Function tooltipList
}

val enchantingTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (!item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "enchantment_wipe_chance", (TurtlematicConfig.enchantmentWipeChance * 100).toInt()))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "enchantment_no_wipe"))
        tooltipList.add(text(TurtlematicCore.MOD_ID, "enchantment_treasure_allowed"))
    }
    return@Function tooltipList
}

val husbandryTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore || !TurtlematicConfig.husbandryAutomataRandomTicksEnabled) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "can_disable_animal_ai"))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.MASTERPIECE)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "periodical_area_grown_accelerator"))
    } else if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "periodical_single_grown_accelerator"))
    }
    return@Function tooltipList
}

val tradingTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    tooltipList.add(text(TurtlematicCore.MOD_ID, "has_trade_abilities"))
    if (item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(text(TurtlematicCore.MOD_ID, "can_restock_traders"))
    }
    return@Function tooltipList
}
