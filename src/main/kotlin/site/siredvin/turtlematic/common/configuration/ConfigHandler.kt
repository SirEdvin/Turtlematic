package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.fml.config.ModConfig
import site.siredvin.turtlematic.Turtlematic


object ConfigHandler {

    fun onLoad(modConfig: ModConfig) {
        if (modConfig.modId == Turtlematic.MOD_ID)
            bakeCommon()
    }

    private fun bakeCommon() {
        //Mechanical soul
        TurtlematicConfig.energyToFuelRate = ConfigHolder.COMMON_CONFIG.ENERGY_TO_FUEL_RATE.get()
        TurtlematicConfig.enableAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_AUTOMATA_CORE.get()
        TurtlematicConfig.enableEndAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_END_AUTOMATA_CORE.get()
        TurtlematicConfig.enableHusbandryAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_HUSBANDRY_AUTOMATA_CORE.get()
        TurtlematicConfig.endAutomataCoreWarpPointLimit =
            ConfigHolder.COMMON_CONFIG.END_AUTOMATA_CORE_WARP_POINT_LIMIT.get()
        TurtlematicConfig.overpoweredAutomataBreakChance =
            ConfigHolder.COMMON_CONFIG.OVERPOWERED_AUTOMATA_BREAK_CHANCE.get()
    }
}