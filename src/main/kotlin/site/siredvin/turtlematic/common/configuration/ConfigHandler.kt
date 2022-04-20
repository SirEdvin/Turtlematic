package site.siredvin.turtlematic.common.configuration

import net.minecraftforge.api.fml.event.config.ModConfigEvent
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.Turtlematic


object ConfigHandler {

    fun register() {
        println(ConfigHolder.COMMON_SPEC)
        ModConfigEvent.LOADING.register(ModConfigEvent.Loading { modConfig ->
            run {
                if (modConfig.modId == Turtlematic.MOD_ID && modConfig.getSpec<ForgeConfigSpec>() == ConfigHolder.COMMON_SPEC) {
                    bakeCommon()
                }
            }
        })

        ModConfigEvent.RELOADING.register(ModConfigEvent.Reloading { modConfig ->
            run {
                if (modConfig.modId == Turtlematic.MOD_ID && modConfig.getSpec<ForgeConfigSpec>() == ConfigHolder.COMMON_SPEC) {
                    bakeCommon()
                }
            }
        })
    }

    private fun bakeCommon() {
        //Defaults
        TurlematicConfig.defaultChatBoxPrefix = ConfigHolder.COMMON_CONFIG.DEFAULT_CHAT_BOX_PREFIX.get()

        //Restrictions
        TurlematicConfig.playerDetMaxRange = ConfigHolder.COMMON_CONFIG.PLAYER_DET_MAX_RANGE.get()
        TurlematicConfig.energyDetectorMaxFlow = ConfigHolder.COMMON_CONFIG.ENERGY_DETECTOR_MAX_FLOW.get()
        TurlematicConfig.poweredPeripheralMaxEnergyStored =
            ConfigHolder.COMMON_CONFIG.POWERED_PERIPHERAL_MAX_ENERGY_STORED.get()
        TurlematicConfig.nbtStorageMaxSize = ConfigHolder.COMMON_CONFIG.NBT_STORAGE_MAX_SIZE.get()
        TurlematicConfig.chunkLoadValidTime = ConfigHolder.COMMON_CONFIG.CHUNK_LOAD_VALID_TIME.get()

        //Features
        TurlematicConfig.enableChatBox = ConfigHolder.COMMON_CONFIG.ENABLE_CHAT_BOX.get()
        TurlematicConfig.enableMeBridge = ConfigHolder.COMMON_CONFIG.ENABLE_ME_BRIDGE.get()
        TurlematicConfig.enableRsBridge = ConfigHolder.COMMON_CONFIG.ENABLE_RS_BRIDGE.get()
        TurlematicConfig.enablePlayerDetector = ConfigHolder.COMMON_CONFIG.ENABLE_PLAYER_DETECTOR.get()
        TurlematicConfig.enableEnvironmentDetector = ConfigHolder.COMMON_CONFIG.ENABLE_ENVIRONMENT_DETECTOR.get()
        TurlematicConfig.enableChunkyTurtle = ConfigHolder.COMMON_CONFIG.ENABLE_CHUNKY_TURTLE.get()
        TurlematicConfig.enableDebugMode = ConfigHolder.COMMON_CONFIG.ENABLE_DEBUG_MODE.get()
        TurlematicConfig.enableEnergyDetector = ConfigHolder.COMMON_CONFIG.ENABLE_ENERGY_DETECTOR.get()
        TurlematicConfig.enableARGoggles = ConfigHolder.COMMON_CONFIG.ENABLE_AR_GOGGLES.get()
        TurlematicConfig.enableInventoryManager = ConfigHolder.COMMON_CONFIG.ENABLE_INVENTORY_MANAGER.get()
        TurlematicConfig.enableRedstoneIntegrator = ConfigHolder.COMMON_CONFIG.ENABLE_REDSTONE_INTEGRATOR.get()
        TurlematicConfig.enableBlockReader = ConfigHolder.COMMON_CONFIG.ENABLE_BLOCK_READER.get()
        TurlematicConfig.enableGeoScanner = ConfigHolder.COMMON_CONFIG.ENABLE_GEO_SCANNER.get()
        TurlematicConfig.enableColonyIntegrator = ConfigHolder.COMMON_CONFIG.ENABLE_COLONY_INTEGRATOR.get()
        TurlematicConfig.enableNBTStorage = ConfigHolder.COMMON_CONFIG.ENABLE_NBT_STORAGE.get()
        TurlematicConfig.enablePoweredPeripherals = ConfigHolder.COMMON_CONFIG.ENABLE_POWERED_PERIPHERALS.get()

        //Mechanical soul
        TurlematicConfig.energyToFuelRate = ConfigHolder.COMMON_CONFIG.ENERGY_TO_FUEL_RATE.get()
        TurlematicConfig.enableWeakAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_WEAK_AUTOMATA_CORE.get()
        TurlematicConfig.enableEndAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_END_AUTOMATA_CORE.get()
        TurlematicConfig.enableHusbandryAutomataCore = ConfigHolder.COMMON_CONFIG.ENABLE_HUSBANDRY_AUTOMATA_CORE.get()
        TurlematicConfig.endAutomataCoreWarpPointLimit =
            ConfigHolder.COMMON_CONFIG.END_AUTOMATA_CORE_WARP_POINT_LIMIT.get()
        TurlematicConfig.overpoweredAutomataBreakChance =
            ConfigHolder.COMMON_CONFIG.OVERPOWERED_AUTOMATA_BREAK_CHANCE.get()

        //World
        TurlematicConfig.givePlayerBookOnJoin = ConfigHolder.COMMON_CONFIG.GIVE_PLAYER_BOOK_ON_JOIN.get()
        TurlematicConfig.enableVillagerStructures = ConfigHolder.COMMON_CONFIG.ENABLE_VILLAGER_STRUCTURES.get()
    }
}