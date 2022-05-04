package site.siredvin.turtlematic.common.configuration

import net.minecraft.client.gui.Font
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.LibConfig
import site.siredvin.lib.api.IConfigHandler
import site.siredvin.lib.computercraft.peripheral.operation.UnconditionalOperations
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

    val enablePistonTurtle: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_TURTLE_PISTON.get()

    val enableStickyPistonTurtle: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_STICKY_TURTLE_PISTON.get()

    val enableLavaBucket: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_LAVA_BUCKET.get()

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

    val durabilityRestoreChance: Double
        get() = ConfigHolder.COMMON_CONFIG.DURABILITY_RESTORE_CHANCE.get()

    val starboundAutomataFuelGenerationChance: Double
        get() = ConfigHolder.COMMON_CONFIG.STARBOUND_AUTOMATA_FUEL_GENERATION_CHANCE.get()

    val starboundAutomataFuelGenerationAmount: Int
        get() = ConfigHolder.COMMON_CONFIG.STARBOUND_AUTOMATA_FUEL_GENERATION_AMOUNT.get()

    // forged automata cores configuration
    val brewingXPReward: Double
        get() = ConfigHolder.COMMON_CONFIG.BREWING_XP_REWARD.get()

    val enchantmentWipeChance: Double
        get() = ConfigHolder.COMMON_CONFIG.ENCHANTING_WIPE_CHANGE.get()

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {
        // Extra turtle peripherals
        val ENABLE_TURTLE_CHATTER: ForgeConfigSpec.BooleanValue
        val ENABLE_CREATIVE_CHEST: ForgeConfigSpec.BooleanValue
        val ENABLE_TURTLE_PISTON: ForgeConfigSpec.BooleanValue
        val ENABLE_STICKY_TURTLE_PISTON: ForgeConfigSpec.BooleanValue
        val ENABLE_LAVA_BUCKET: ForgeConfigSpec.BooleanValue
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

        val STARBOUND_AUTOMATA_FUEL_GENERATION_CHANCE: ForgeConfigSpec.DoubleValue
        val STARBOUND_AUTOMATA_FUEL_GENERATION_AMOUNT: ForgeConfigSpec.IntValue
        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue
        val DURABILITY_RESTORE_CHANCE: ForgeConfigSpec.DoubleValue
        val BREWING_XP_REWARD: ForgeConfigSpec.DoubleValue
        val ENCHANTING_WIPE_CHANGE: ForgeConfigSpec.DoubleValue

        init {
            LibConfig.build(builder)
            builder.push("turtlePeripherals")
            ENABLE_TURTLE_CHATTER = builder.define("enableTurtleChatter", true)
            ENABLE_CREATIVE_CHEST = builder.define("enableCreativeChest", true)
            ENABLE_TURTLE_PISTON = builder.define("enablePistonTurtle", true)
            ENABLE_STICKY_TURTLE_PISTON = builder.define("enableStickyPistonTurtle", true)
            ENABLE_LAVA_BUCKET = builder.define("enableLavaBucket", true)
            builder.pop()
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            register(CountOperation.values(), builder)
            register(UnconditionalOperations.values(), builder)
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

            END_AUTOMATA_CORE_WARP_POINT_LIMIT = builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)
            DURABILITY_RESTORE_CHANCE = builder.comment("Defined chance to not loose item durability for netherite cores")
                .defineInRange("durabilityRestoreChance", 0.1, 0.0, 1.0)
            STARBOUND_AUTOMATA_FUEL_GENERATION_CHANCE = builder.comment("Defines chance for starbound automata to regenerate fuel points")
                .defineInRange("starboundAutomataFuelGenerationChance", 0.15, 0.0, 1.0)
            STARBOUND_AUTOMATA_FUEL_GENERATION_AMOUNT = builder.comment("Defines amount for starbound automata that will be regenerated")
                .defineInRange("starboundAutomataFuelGenerationAmount", 1, 0, Integer.MAX_VALUE)
            BREWING_XP_REWARD = builder.defineInRange("brewingXPReward", 0.8, 0.0, 64.0)
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