package site.siredvin.turtlematic.util

import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item.TooltipContext
import net.minecraft.world.item.ItemStack
import site.siredvin.broccolium.modules.base.item.HiddenDescriptiveItemItem
import site.siredvin.broccolium.modules.platform.PlatformToolkit
import site.siredvin.turtlematic.api.AutomataCoreTraits
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.computercraft.plugins.AutomataCapturePlugin
import site.siredvin.turtlematic.data.ModTooltip
import site.siredvin.tweakium.modules.peripheral.ability.ExperienceBoon
import site.siredvin.tweakium.modules.peripheral.api.InteractionMode
import site.siredvin.tweakium.modules.peripheral.util.CompoundTagDataStorage
import java.util.function.BiFunction
import java.util.function.Function

val isDisabled = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
    if (!item.isEnabled()) {
        return@Function listOf(ModTooltip.ITEM_DISABLED.text)
    }
    return@Function emptyList()
}

val commonTooltips = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
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

val itemUsageTooltip = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
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

val enchantingTooltip = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
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

val husbandryTooltip = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(ModTooltip.CAN_DISABLE_ANIMAL_AI.text)
    }
    return@Function tooltipList
}

val tradingTooltip = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
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

val protectiveTooltip = Function<HiddenDescriptiveItemItem, List<Component>> { item ->
    if (item !is BaseAutomataCore) {
        return@Function emptyList()
    }
    val tooltipList = mutableListOf<Component>()
    if (item.coreTier.traits.contains(AutomataCoreTraits.APPRENTICE)) {
        tooltipList.add(ModTooltip.CAN_DISABLE_HOSTILE_AI.text)
    }
    return@Function tooltipList
}

val capturedTooltip = BiFunction<ItemStack, TooltipContext, List<Component>> { it, level ->
    if (it.item !is BaseAutomataCore) return@BiFunction emptyList()
    val dataTag = it.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: return@BiFunction emptyList()
    val dataStorage = CompoundTagDataStorage(dataTag) {}
    val capturedType = AutomataCapturePlugin.getStoredType(dataStorage) ?: return@BiFunction emptyList()
    return@BiFunction when (capturedType) {
        InteractionMode.BLOCK -> listOf(ModTooltip.CAPTURED_BLOCK.format(AutomataCapturePlugin.extractBlock(dataStorage)!!.first.block.name.string))
        InteractionMode.ENTITY -> {
            if (level != null) {
                listOf(ModTooltip.CAPTURED_ENTITY.format(AutomataCapturePlugin.extractEntity(dataStorage, PlatformToolkit.get().minecraftServer?.overworld()!!)!!.name.string))
            } else {
                emptyList()
            }
        }
        else -> emptyList()
    }
}

val xpTooltip = BiFunction<ItemStack, TooltipContext, List<Component>> { stack, _ ->
    if (stack.item !is BaseAutomataCore) return@BiFunction emptyList()
    val dataStorage = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: return@BiFunction emptyList()
    val storedXP = ExperienceBoon.getStoredXP(CompoundTagDataStorage(dataStorage) {})
    if (storedXP < 1) return@BiFunction emptyList()
    return@BiFunction listOf(ModTooltip.AMOUNT_OF_XP.format(storedXP))
}
