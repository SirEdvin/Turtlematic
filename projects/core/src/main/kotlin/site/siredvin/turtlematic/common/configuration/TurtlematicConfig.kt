package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.config.IConfigHandler
import site.siredvin.peripheralium.api.config.IOperationAbilityConfig
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.computercraft.operations.*

object TurtlematicConfig : IOperationAbilityConfig {
    override val cooldownTresholdLevel: Int
        get() = ConfigHolder.COMMON_CONFIG.COOLDOWN_TRESHOLD_LEVEL.get()
    val xpToFuelRate: Int
        get() = ConfigHolder.COMMON_CONFIG.XP_TO_FUEL_RATE.get()

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

    val enableChunkVial: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_CHUNK_VIAL.get()

    val chunkLoadedTimeLimit: Long
        get() = ConfigHolder.COMMON_CONFIG.CHUNK_VIAL_TIME_LIMIT.get()

    val enableBowTurtle: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_BOW_TURTLE.get()

    val enableMimicGadget: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_MIMIC_GADGET.get()

    val mimicGadgetRMLLimit: Int
        get() = ConfigHolder.COMMON_CONFIG.MIMIC_GADGET_RML_LIMIT.get()

    // automata core toggles
    val enableAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_AUTOMATA_CORE.get()
    val enableEndAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_END_AUTOMATA_CORE.get()
    val enableHusbandryAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_HUSBANDRY_AUTOMATA_CORE.get()
    val enableProtectiveAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_PROTECTIVE_AUTOMATA_CORE.get()
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
    val enableMercantileAutomataCore: Boolean
        get() = ConfigHolder.COMMON_CONFIG.ENABLE_MERCANTILE_AUTOMATA_CORE.get()

    // automata cores configuration
    val endAutomataCoreWarpPointLimit: Int
        get() = ConfigHolder.COMMON_CONFIG.END_AUTOMATA_CORE_WARP_POINT_LIMIT.get()

    val husbandryAutomataRandomTicksEnabled: Boolean
        get() = ConfigHolder.COMMON_CONFIG.HUSBANDRY_AUTOMATA_RANDOM_TICKS_ENABLED.get()

    val netheriteHusbandryAutomataGrownPeriod: Int
        get() = ConfigHolder.COMMON_CONFIG.NETHERITE_HUSBANDRY_AUTOMATA_GROWN_PERIOD.get()

    val starboundeHusbandryAutomataGrownPeriod: Int
        get() = ConfigHolder.COMMON_CONFIG.STARBOUND_HUSBANDRY_AUTOMATA_GROWN_PERIOD.get()

    val creativeHusbandryAutomataGrownPeriod: Int
        get() = ConfigHolder.COMMON_CONFIG.CREATIVE_HUSBANDRY_AUTOMATA_GROWN_PERIOD.get()

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
        // Generic configuration
        var COOLDOWN_TRESHOLD_LEVEL: ForgeConfigSpec.IntValue
        var XP_TO_FUEL_RATE: ForgeConfigSpec.IntValue

        // Extra turtle peripherals
        val ENABLE_TURTLE_CHATTER: ForgeConfigSpec.BooleanValue
        val ENABLE_CREATIVE_CHEST: ForgeConfigSpec.BooleanValue
        val ENABLE_TURTLE_PISTON: ForgeConfigSpec.BooleanValue
        val ENABLE_STICKY_TURTLE_PISTON: ForgeConfigSpec.BooleanValue
        val ENABLE_LAVA_BUCKET: ForgeConfigSpec.BooleanValue
        val ENABLE_CHUNK_VIAL: ForgeConfigSpec.BooleanValue
        val CHUNK_VIAL_TIME_LIMIT: ForgeConfigSpec.LongValue
        val ENABLE_BOW_TURTLE: ForgeConfigSpec.BooleanValue
        val ENABLE_MIMIC_GADGET: ForgeConfigSpec.BooleanValue
        val MIMIC_GADGET_RML_LIMIT: ForgeConfigSpec.IntValue

        // Automata Core
        val ENABLE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_END_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_HUSBANDRY_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_PROTECTIVE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENORMOUS_AUTOMATA: ForgeConfigSpec.BooleanValue

        // Forged automata core
        val ENABLE_BREWING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_SMITHING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENCHANTING_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_MASON_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_MERCANTILE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue

        val STARBOUND_AUTOMATA_FUEL_GENERATION_CHANCE: ForgeConfigSpec.DoubleValue
        val STARBOUND_AUTOMATA_FUEL_GENERATION_AMOUNT: ForgeConfigSpec.IntValue
        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue
        val HUSBANDRY_AUTOMATA_RANDOM_TICKS_ENABLED: ForgeConfigSpec.BooleanValue
        val NETHERITE_HUSBANDRY_AUTOMATA_GROWN_PERIOD: ForgeConfigSpec.IntValue
        val STARBOUND_HUSBANDRY_AUTOMATA_GROWN_PERIOD: ForgeConfigSpec.IntValue
        val CREATIVE_HUSBANDRY_AUTOMATA_GROWN_PERIOD: ForgeConfigSpec.IntValue
        val DURABILITY_RESTORE_CHANCE: ForgeConfigSpec.DoubleValue
        val BREWING_XP_REWARD: ForgeConfigSpec.DoubleValue
        val ENCHANTING_WIPE_CHANGE: ForgeConfigSpec.DoubleValue

        init {
            builder.push("base")
            COOLDOWN_TRESHOLD_LEVEL = builder.comment("Determinates trashold for cooldown to be stored")
                .defineInRange("cooldownTreshholdLevel", 100, 0, Int.MAX_VALUE)
            XP_TO_FUEL_RATE = builder.comment("Determinate amount xp to correspond one fuel point").defineInRange("xpToFuelRate", 10, 1, Int.MAX_VALUE)
            builder.pop()
            builder.push("turtlePeripherals")
            ENABLE_TURTLE_CHATTER = builder.define("enableTurtleChatter", true)
            ENABLE_CREATIVE_CHEST = builder.define("enableCreativeChest", true)
            ENABLE_TURTLE_PISTON = builder.define("enablePistonTurtle", true)
            ENABLE_STICKY_TURTLE_PISTON = builder.define("enableStickyPistonTurtle", true)
            ENABLE_LAVA_BUCKET = builder.define("enableLaaBucket", true)
            ENABLE_CHUNK_VIAL = builder.define("enableChunkVial", true)
            CHUNK_VIAL_TIME_LIMIT = builder.comment("Soft limit for chunk to be loaded until turtle register it again, in milliseconds")
                .defineInRange("chunkVialTimeLimit", 5_000, 1, Long.MAX_VALUE)
            ENABLE_BOW_TURTLE = builder.define("enableBowTurtle", true)
            ENABLE_MIMIC_GADGET = builder.define("enableMimicGadget", true)
            MIMIC_GADGET_RML_LIMIT = builder.comment("Defines limit of RML instructions")
                .defineInRange("mimicGadgetRMLLimit", 8, 0, 128)
            builder.pop()
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            register(CountOperation.values(), builder)
            register(UnconditionalOperation.values(), builder)
            register(PowerOperation.values(), builder)
            builder.pop()
            builder.push("automataCores")
            ENABLE_AUTOMATA_CORE = builder.define("enableWeakAutomataCore", true)
            ENABLE_END_AUTOMATA_CORE = builder.define("enableEndAutomataCore", true)
            ENABLE_HUSBANDRY_AUTOMATA_CORE = builder.define("enableHusbandryAutomataCore", true)
            ENABLE_PROTECTIVE_AUTOMATA_CORE = builder.define("enableProtectiveAutomataCore", true)
            ENABLE_ENORMOUS_AUTOMATA = builder.define("enableEnormousAutomata", true)

            ENABLE_BREWING_AUTOMATA_CORE = builder.define("enableBrewingAutomataCore", true)
            ENABLE_SMITHING_AUTOMATA_CORE = builder.define("enableSmithingAutomataCore", true)
            ENABLE_ENCHANTING_AUTOMATA_CORE = builder.define("enableEnchantingAutomataCore", true)
            ENABLE_MASON_AUTOMATA_CORE = builder.define("enableMasonAutomataCore", true)
            ENABLE_MERCANTILE_AUTOMATA_CORE = builder.define("enableMercantileAutomataCore", true)

            END_AUTOMATA_CORE_WARP_POINT_LIMIT = builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)
            HUSBANDRY_AUTOMATA_RANDOM_TICKS_ENABLED = builder.comment("Enables random ticks for starbound and netherite husbandry automata core")
                .define("husbandryAutomataRandomTicksEnabled", true)
            NETHERITE_HUSBANDRY_AUTOMATA_GROWN_PERIOD = builder.comment("Amount of ticks that separate single random tick for one of surrounding crops for netherite husbandry automata")
                .defineInRange("netheriteHusbandryAutomataGrownPeriod", 10, 1, Int.MAX_VALUE)
            STARBOUND_HUSBANDRY_AUTOMATA_GROWN_PERIOD = builder.comment("Amount of ticks that separate single random tick for one of surrounding crops for starbound husbandry automata")
                .defineInRange("starboundHusbandryAutomataGrownPeriod", 2, 1, Int.MAX_VALUE)
            CREATIVE_HUSBANDRY_AUTOMATA_GROWN_PERIOD = builder.comment("Amount of ticks that separate single random tick for all of surrounding crops for creative husbandry automata")
                .defineInRange("creativeHusbandryAutomataGrownPeriod", 40, 1, Int.MAX_VALUE)
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
