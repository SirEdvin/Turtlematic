package site.siredvin.turtlematic.util

import net.minecraft.network.chat.Component
import site.siredvin.peripheralium.common.items.PeripheralItem
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.data.ModTooltip
import java.util.function.Function

val isDisabled = Function<PeripheralItem, List<Component>> { item ->
    if (!item.isEnabled()) {
        return@Function listOf(ModTooltip.ITEM_DISABLED.text)
    }
    return@Function emptyList()
}

val commonTooltips = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    tooltipList.add(ModTooltip.CORE_CONFIGURATION.text)
    tooltipList.add(ModTooltip.INTERACTION_RADIUS.format(item.coreTier.interactionRadius))
    tooltipList.add(ModTooltip.MAX_FUEL_CONSUMPTION_RATE.format(item.coreTier.maxFuelConsumptionRate))
    if (item.coreTier.cooldownReduceFactor != 1.0) {
        tooltipList.add(ModTooltip.COOLDOWN_REDUCE_FACTOR.format(item.coreTier.cooldownReduceFactor))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.STARBOUND_REGENERATION)) {
        tooltipList.add(ModTooltip.STARBOUND_GENERATION.text)
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.FUEL_CONSUMPTION_DISABLED)) {
        tooltipList.add(ModTooltip.FUEL_CONSUMPTION_DISABLED.text)
    }
    return@Function tooltipList
}

val itemUsageTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND_CHANCE)) {
        tooltipList.add(ModTooltip.DURABILITY_REFUND_CHANCE.text)
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.DURABILITY_REFUND)) {
        tooltipList.add(ModTooltip.DURABILITY_REFUND.text)
    }
    return@Function tooltipList
}

val enchantingTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (!item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(ModTooltip.ENCHANTMENT_WIPE_CHANCE.format((TurtlematicConfig.enchantmentWipeChance * 100).toInt()))
    }
    if (item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(ModTooltip.ENCHANTMENT_NO_WIPE.text)
        tooltipList.add(ModTooltip.ENCHANTMENT_TREASURE_ALLOWED.text)
    }
    return@Function tooltipList
}

val husbandryTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(ModTooltip.CAN_DISABLE_ANIMAL_AI.text)
    }
    if (TurtlematicConfig.husbandryAutomataRandomTicksEnabled) {
        // So, this condition is a little strange, but main idea here is core tier has both traits
        // if it has MASTERPIECE, so we need this trick
        if (item.coreTier.traits.contains(AutomataCoreTraits.MASTERPIECE)) {
            tooltipList.add(ModTooltip.AREA_GROWN.text)
        } else if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
            tooltipList.add(ModTooltip.SINGLE_GROWN.text)
        }
    }
    return@Function tooltipList
}

val tradingTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    tooltipList.add(ModTooltip.HAS_TRADE_ABILITIES.text)
    if (item.coreTier.traits.contains(AutomataCoreTraits.SKILLED)) {
        tooltipList.add(ModTooltip.CAN_RESTORE_TRADES.text)
    }
    return@Function tooltipList
}

val protectiveTooltip = Function<PeripheralItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(ModTooltip.CAN_DISABLE_HOSTILE_AI.text)
    }
    return@Function tooltipList
}
