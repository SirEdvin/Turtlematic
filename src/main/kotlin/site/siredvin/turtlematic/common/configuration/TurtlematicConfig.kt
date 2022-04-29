package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.api.IConfigHandler
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.computercraft.operations.CountOperation
import site.siredvin.turtlematic.computercraft.operations.SimpleFreeOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.operations.SphereOperation

object TurtlematicConfig {
    // additonal turtle peripherals
    val enableTurtleChatter: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_TURTLE_CHATTER.get()

    val enableCreativeChest: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_CREATIVE_CHEST.get()

    // automata core toggles
    val enableAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_AUTOMATA_CORE.get()
    val enableEndAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_END_AUTOMATA_CORE.get()
    val enableHusbandryAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_HUSBANDRY_AUTOMATA_CORE.get()
    val enableEnormousAutomata: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_ENORMOUS_AUTOMATA.get()
    // forged automata cores toggles
    val enableBrewingAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_BREWING_AUTOMATA_CORE.get()

    val enableSmithingAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_SMITHING_AUTOMATA_CORE.get()

    val enableEnchantingAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_ENCHANTING_AUTOMATA_CORE.get()

    val enableMasonAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_MASON_AUTOMATA_CORE.get()

    // automata cores configuration
    val endAutomataCoreWarpPointLimit: Int
        get() = ConfigHolder.COMMON_CONFIG.END_AUTOMATA_CORE_WARP_POINT_LIMIT.get()

    // forged automata cores configuration
    val brewingXPReward: Double
        get() = ConfigHolder.COMMON_CONFIG.BREWING_XP_REWARD.get()

    val enchantLevelCost: Int
        get() = ConfigHolder.COMMON_CONFIG.ENCHANT_LEVEL_COST.get()

    val enchantmentWipeChance: Double
        get() = ConfigHolder.COMMON_CONFIG.ENCHANTING_WIPE_CHANGE.get()

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {
        // Extra turtle peripherals
        val ENABLE_TURTLE_CHATTER: ForgeConfigSpec.BooleanValue
        val ENABLE_CREATIVE_CHEST: ForgeConfigSpec.BooleanValue
        // Automata Core
        val ENABLE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_END_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_HUSBANDRY_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENORMOUS_AUTOMATA: ForgeConfigSpec.BooleanValue
        // Forged automata core
        val ENABLE_BREWING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_SMITHING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENCHANTING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_MASON_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue

        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue
        val BREWING_XP_REWARD: ForgeConfigSpec.DoubleValue
        val ENCHANTING_WIPE_CHANGE: ForgeConfigSpec.DoubleValue
        val ENCHANT_LEVEL_COST: ForgeConfigSpec.IntValue

        init {
            builder.push("turtlePeripherals")
            ENABLE_TURTLE_CHATTER = builder.define("enableTurtleChatter", true)
            ENABLE_CREATIVE_CHEST = builder.define("enableCreativeChest", true)
            builder.pop()
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            register(CountOperation.values(), builder)
            builder.pop()
            builder.push("automataCores")
            ENABLE_AUTOMATA_CORE = builder.define("enableWeakAutomataCore", true)
            ENABLE_END_AUTOMATA_CORE = builder.define("enableEndAutomataCore", true)
            ENABLE_HUSBANDRY_AUTOMATA_CORE = builder.define("enableHusbandryAutomataCore", true)
            ENABLE_ENORMOUS_AUTOMATA = builder.define("enableEnormousAutomata", true)

            ENABLE_BREWING_AUTOMATA_CORE = builder.define("enableBrewingAutomataCore", true)
            ENABLE_SMITHING_AUTOMATA_CORE = builder.define("enableSmithingAutomataCore", true)
            ENABLE_ENCHANTING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true)
            ENABLE_MASON_AUTOMATA_CORE = builder.define("enableMasonAutomataCore", true)

            END_AUTOMATA_CORE_WARP_POINT_LIMIT =
                builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                    .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)
            BREWING_XP_REWARD = builder.defineInRange("brewingXPReward", 0.8, 0.0, 64.0)
            ENCHANT_LEVEL_COST = builder.defineInRange("enchantLevelCost", 30, 1, Int.MAX_VALUE)
            ENCHANTING_WIPE_CHANGE = builder.defineInRange("enchantmentWipeChance", 0.05, 0.1, 1.0)

            // automata core tiers registration
            register(AutomataCoreTier.values(), builder)
            builder.pop()
        }

        private fun register(data: Array<out IConfigHandler>, builder: ForgeConfigSpec.Builder) {
            for (handler in data) {
                handler.addToConfig(builder)
            }
        }
    }
}