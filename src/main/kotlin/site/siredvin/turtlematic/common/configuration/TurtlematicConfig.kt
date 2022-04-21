package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.misc.IConfigHandler
import site.siredvin.lib.operations.AutomataCoreTier
import site.siredvin.lib.operations.SimpleFreeOperation
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.operations.SphereOperation

object TurtlematicConfig {

    // automata cores configuration
    var energyToFuelRate = 0
    var enableAutomataCore = false
    var enableEndAutomataCore = false
    var enableHusbandryAutomataCore = false
    var endAutomataCoreWarpPointLimit = 0
    var overpoweredAutomataBreakChance = 0.0

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {

        // Automata Core
        val ENERGY_TO_FUEL_RATE: ForgeConfigSpec.IntValue
        val ENABLE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_END_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_HUSBANDRY_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue
        val OVERPOWERED_AUTOMATA_BREAK_CHANCE: ForgeConfigSpec.DoubleValue

        init {
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            builder.pop()
            builder.push("metaphysics")
            ENERGY_TO_FUEL_RATE = builder.comment("Defines energy to fuel rate")
                .defineInRange("energyToFuelRate", 575, 575, Int.MAX_VALUE)
            ENABLE_AUTOMATA_CORE = builder.define("enableWeakAutomataCore", true)
            ENABLE_END_AUTOMATA_CORE = builder.define("enableEndAutomataCore", true)
            ENABLE_HUSBANDRY_AUTOMATA_CORE = builder.define("enableHusbandryAutomataCore", true)
            END_AUTOMATA_CORE_WARP_POINT_LIMIT =
                builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                    .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)
            OVERPOWERED_AUTOMATA_BREAK_CHANCE =
                builder.comment("Chance that overpowered automata will break after rotation cycle")
                    .defineInRange("overpoweredAutomataBreakChance", 0.002, 0.0, 1.0)

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