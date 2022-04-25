package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.fml.config.ModConfig
import site.siredvin.turtlematic.Turtlematic


object ConfigHandler {

    fun onLoad(modConfig: ModConfig) {
        if (modConfig.modId == Turtlematic.MOD_ID)
            bakeCommon()
    }

    private fun bakeCommon() {
        // Turtle peripherals
        TurtlematicConfig.enableTurtleChatter = ConfigHolder.COMMON_CONFIG.ENABLE_TURTLE_CHATTER.get()
        //Mechanical soul
        TurtlematicConfig.enableAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_AUTOMATA_CORE.get()
        TurtlematicConfig.enableEndAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_END_AUTOMATA_CORE.get()
        TurtlematicConfig.enableHusbandryAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_HUSBANDRY_AUTOMATA_CORE.get()
        TurtlematicConfig.enableEnormousAutomata = ConfigHolder.COMMON_CONFIG.ENABLE_ENORMOUS_AUTOMATA.get()

        TurtlematicConfig.endAutomataCoreWarpPointLimit = ConfigHolder.COMMON_CONFIG.END_AUTOMATA_CORE_WARP_POINT_LIMIT.get()
    }
}