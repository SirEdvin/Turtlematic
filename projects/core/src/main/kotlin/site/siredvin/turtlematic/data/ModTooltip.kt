package site.siredvin.turtlematic.data

import site.siredvin.peripheralium.data.language.TextRecord
import site.siredvin.turtlematic.TurtlematicCore

enum class ModTooltip: TextRecord{
    ITEM_DISABLED,
    CONSUMED_ENTITIES,
    CONSUMED_ENTITIES_RECORD,
    CORE_CONFIGURATION,
    INTERACTION_RADIUS,
    MAX_FUEL_CONSUMPTION_RATE,
    COOLDOWN_REDUCE_FACTOR,
    PRESS_FOR_RECIPE,
    RECIPE_MISSING,
    SOUL_UPGRADE_FROM,
    REQUIRED_SOULS,
    DURABILITY_REFUND_CHANCE,
    DURABILITY_REFUND,
    STARBOUND_GENERATION,
    ENCHANTMENT_WIPE_CHANCE,
    ENCHANTMENT_NO_WIPE,
    ENCHANTMENT_TREASURE_ALLOWED,
    FUEL_CONSUMPTION_DISABLED,
    SINGLE_GROWN,
    AREA_GROWN,
    CAN_DISABLE_ANIMAL_AI,
    CAN_DISABLE_HOSTILE_AI,
    HAS_TRADE_ABILITIES,
    CAN_RESTORE_TRADES,
    SOUL_VIAL_PROGRESS
    ;

    override val textID: String by lazy {
        String.format("tooltip.%s.%s", TurtlematicCore.MOD_ID, name.lowercase())
    }
}