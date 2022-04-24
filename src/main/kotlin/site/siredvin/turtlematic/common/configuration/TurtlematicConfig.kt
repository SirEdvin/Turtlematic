package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.misc.IConfigHandler
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.integrations.computercraft.operations.SimpleFreeOperation
import site.siredvin.turtlematic.integrations.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.integrations.computercraft.operations.SphereOperation

object TurtlematicConfig {

    // automata core toggles
    var enableAutomataCore = false
    var enableEndAutomataCore = false
    var enableHusbandryAutomataCore = false
    var enableEnormousAutomata = false

    // automata cores configuration
    var endAutomataCoreWarpPointLimit = 0

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {

        // Automata Core
        val ENABLE_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_END_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_HUSBANDRY_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENORMOUS_AUTOMATA: ForgeConfigSpec.BooleanValue

        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue

        init {
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            builder.pop()
            builder.push("metaphysics")
            ENABLE_AUTOMATA_CORE = builder.define("enableWeakAutomataCore", true)
            ENABLE_END_AUTOMATA_CORE = builder.define("enableEndAutomataCore", true)
            ENABLE_HUSBANDRY_AUTOMATA_CORE = builder.define("enableHusbandryAutomataCore", true)
            ENABLE_ENORMOUS_AUTOMATA = builder.define("enableEnormousAutomata", true)
            END_AUTOMATA_CORE_WARP_POINT_LIMIT =
                builder.comment("Defines max warp point stored in warp core. Mostly need to not allow NBT overflow error")
                    .defineInRange("endAutomataCoreWarpPointLimit", 64, 1, Int.MAX_VALUE)

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