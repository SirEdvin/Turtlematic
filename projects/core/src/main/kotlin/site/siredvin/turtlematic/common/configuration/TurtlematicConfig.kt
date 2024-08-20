package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.config.IConfigHandler
import site.siredvin.peripheralium.api.config.IOperationAbilityConfig
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.computercraft.operations.*

object TurtlematicConfig : IOperationAbilityConfig {
    override val cooldownTresholdLevel: Int
        get() = ConfigHolder.commonConfig.cooldownTresholdLevel.get()
    val xpToFuelRate: Int
        get() = ConfigHolder.commonConfig.xpToFuelRate.get()

    // additonal turtle peripherals
    val enableTurtleChatter: Boolean
        get() = ConfigHolder.commonConfig.enableTurtleChatter.get()

    val enableCreativeChest: Boolean
        get() = ConfigHolder.commonConfig.enableCreativeChest.get()

    val enablePistonTurtle: Boolean
        get() = ConfigHolder.commonConfig.enableTurtlePiston.get()

    val enableStickyPistonTurtle: Boolean
        get() = ConfigHolder.commonConfig.enableStickyTurtlePiston.get()

    val pistonVolumeLevel: Double
        get() = ConfigHolder.commonConfig.pistonVolumeLevel.get()

    val pistonPitchLevel: Double
        get() = ConfigHolder.commonConfig.pistonPitchLevel.get()

    val enableLavaBucket: Boolean
        get() = ConfigHolder.commonConfig.enableLavaBucket.get()

    val enableChunkVial: Boolean
        get() = ConfigHolder.commonConfig.enableChunkVial.get()

    val chunkLoadedTimeLimit: Long
        get() = ConfigHolder.commonConfig.chunkVialTimeLimit.get()

    val enableBowTurtle: Boolean
        get() = ConfigHolder.commonConfig.enableBowTurtle.get()

    val bowTurtlePowerLimit: Double
        get() = ConfigHolder.commonConfig.bowTurtlePowerLimit.get()

    val enableMimicGadget: Boolean
        get() = ConfigHolder.commonConfig.enableMimicGadget.get()

    val mimicGadgetRMLLimit: Int
        get() = ConfigHolder.commonConfig.mimicGadgetRMLLimit.get()

    // automata core toggles
    val enableAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableAutomataCore.get()
    val enableEndAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableEndAutomataCore.get()
    val enableHusbandryAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableHusbandryAutomataCore.get()
    val enableProtectiveAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableProtectiveAutomataCore.get()
    val enableEnormousAutomata: Boolean
        get() = ConfigHolder.commonConfig.enableEnormousAutomataCore.get()

    // forged automata cores toggles
    val enableBrewingAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableBrewingAutomataCore.get()

    val enableSmithingAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableSmitingAutomataCore.get()

    val enableEnchantingAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableEnchantingAutomataCore.get()

    val enableMasonAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableMasonAutomataCore.get()
    val enableMercantileAutomataCore: Boolean
        get() = ConfigHolder.commonConfig.enableMercantileAutomataCore.get()

    // automata cores configuration
    val endAutomataCoreWarpPointLimit: Int
        get() = ConfigHolder.commonConfig.endAutomataCoreWarpPointLimit.get()

    val durabilityRestoreChance: Double
        get() = ConfigHolder.commonConfig.durabilityRestoreChance.get()

    val starboundAutomataFuelGenerationChance: Double
        get() = ConfigHolder.commonConfig.starboundAutomataFuelGenerationChance.get()

    val starboundAutomataFuelGenerationAmount: Int
        get() = ConfigHolder.commonConfig.starboundAutomataFuelGenerationAmount.get()

    // forged automata cores configuration
    val brewingXPReward: Double
        get() = ConfigHolder.commonConfig.brewingXpReward.get()

    val brewingPowerLimit: Double
        get() = ConfigHolder.commonConfig.brewingPowerLimit.get()

    val enchantmentWipeChance: Double
        get() = ConfigHolder.commonConfig.enchantingWipeChance.get()

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {
        // Generic configuration
        var cooldownTresholdLevel: ForgeConfigSpec.IntValue
        var xpToFuelRate: ForgeConfigSpec.IntValue

        // Extra turtle peripherals
        val enableTurtleChatter: ForgeConfigSpec.BooleanValue
        val enableCreativeChest: ForgeConfigSpec.BooleanValue
        val enableTurtlePiston: ForgeConfigSpec.BooleanValue
        val enableStickyTurtlePiston: ForgeConfigSpec.BooleanValue
        val pistonVolumeLevel: ForgeConfigSpec.DoubleValue
        val pistonPitchLevel: ForgeConfigSpec.DoubleValue
        val enableLavaBucket: ForgeConfigSpec.BooleanValue
        val enableChunkVial: ForgeConfigSpec.BooleanValue
        val chunkVialTimeLimit: ForgeConfigSpec.LongValue
        val enableBowTurtle: ForgeConfigSpec.BooleanValue
        val bowTurtlePowerLimit: ForgeConfigSpec.DoubleValue
        val enableMimicGadget: ForgeConfigSpec.BooleanValue
        val mimicGadgetRMLLimit: ForgeConfigSpec.IntValue

        // Automata Core
        val enableAutomataCore: ForgeConfigSpec.BooleanValue
        val enableEndAutomataCore: ForgeConfigSpec.BooleanValue
        val enableHusbandryAutomataCore: ForgeConfigSpec.BooleanValue
        val enableProtectiveAutomataCore: ForgeConfigSpec.BooleanValue
        val enableEnormousAutomataCore: ForgeConfigSpec.BooleanValue

        // Forged automata core
        val enableBrewingAutomataCore: ForgeConfigSpec.BooleanValue
        val enableSmitingAutomataCore: ForgeConfigSpec.BooleanValue
        val enableEnchantingAutomataCore: ForgeConfigSpec.BooleanValue
        val enableMasonAutomataCore: ForgeConfigSpec.BooleanValue
        val enableMercantileAutomataCore: ForgeConfigSpec.BooleanValue

        val starboundAutomataFuelGenerationChance: ForgeConfigSpec.DoubleValue
        val starboundAutomataFuelGenerationAmount: ForgeConfigSpec.IntValue
        val endAutomataCoreWarpPointLimit: ForgeConfigSpec.IntValue
        val durabilityRestoreChance: ForgeConfigSpec.DoubleValue
        val brewingXpReward: ForgeConfigSpec.DoubleValue
        val brewingPowerLimit: ForgeConfigSpec.DoubleValue
        val enchantingWipeChance: ForgeConfigSpec.DoubleValue

        init {
            builder.push("base")
            cooldownTresholdLevel = builder.comment("Determinates trashold for cooldown to be stored")
                .defineInRange("cooldownTreshholdLevel", 100, 0, Int.MAX_VALUE)
            xpToFuelRate = builder.comment("Determinate amount xp to correspond one fuel point").defineInRange("xpToFuelRate", 10, 1, Int.MAX_VALUE)
            builder.pop()
            builder.push("turtlePeripherals")
            enableTurtleChatter = builder.define("enableTurtleChatter", true)
            enableCreativeChest = builder.define("enableCreativeChest", true)
            enableTurtlePiston = builder.define("enablePistonTurtle", true)
            enableStickyTurtlePiston = builder.define("enableStickyPistonTurtle", true)
            pistonPitchLevel = builder.defineInRange("pistonPitchLevel", 5.0, 0.0, 10.0)
            pistonVolumeLevel = builder.defineInRange("pistonVolumeLevel", 5.0, 0.0, 10.0)
            enableLavaBucket = builder.define("enableLaaBucket", true)
            enableChunkVial = builder.define("enableChunkVial", true)
            chunkVialTimeLimit = builder.comment("Soft limit for chunk to be loaded until turtle register it again, in milliseconds")
                .defineInRange("chunkVialTimeLimit", 5_000, 1, Long.MAX_VALUE)
            enableBowTurtle = builder.define("enableBowTurtle", true)
            bowTurtlePowerLimit = builder.defineInRange("bowTurtlePowerLimit", 10.0, 1.0, Double.MAX_VALUE)
            enableMimicGadget = builder.define("enableMimicGadget", true)
            mimicGadgetRMLLimit = builder.comment("Defines limit of RML instructions")
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
            enableAutomataCore = builder.define("enableWeakAutomataCore", true)
            enableEndAutomataCore = builder.define("enableEndAutomataCore", true)
            enableHusbandryAutomataCore = builder.define("enableHusbandryAutomataCore", true)
            enableProtectiveAutomataCore = builder.define("enableProtectiveAutomataCore", true)
            enableEnormousAutomataCore = builder.define("enableEnormousAutomata", true)

            enableBrewingAutomataCore = builder.define("enableBrewingAutomataCore", true)
            enableSmitingAutomataCore = builder.define("enableSmithingAutomataCore", true)
            enableEnchantingAutomataCore = builder.define("enableEnchantingAutomataCore", true)
            enableMasonAutomataCore = builder.define("enableMasonAutomataCore", true)
            enableMercantileAutomataCore = builder.define("enableMercantileAutomataCore", true)

            endAutomataCoreWarpPointLimit = builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)
            durabilityRestoreChance = builder.comment("Defined chance to not loose item durability for netherite cores")
                .defineInRange("durabilityRestoreChance", 0.1, 0.0, 1.0)
            starboundAutomataFuelGenerationChance = builder.comment("Defines chance for starbound automata to regenerate fuel points")
                .defineInRange("starboundAutomataFuelGenerationChance", 0.15, 0.0, 1.0)
            starboundAutomataFuelGenerationAmount = builder.comment("Defines amount for starbound automata that will be regenerated")
                .defineInRange("starboundAutomataFuelGenerationAmount", 1, 0, Integer.MAX_VALUE)
            brewingXpReward = builder.defineInRange("brewingXPReward", 0.8, 0.0, 64.0)
            brewingPowerLimit = builder.defineInRange("brewingPowerLimit", 10.0, 1.0, Double.MAX_VALUE)
            enchantingWipeChance = builder.defineInRange("enchantmentWipeChance", 0.05, 0.1, 1.0)

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
