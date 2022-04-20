package site.siredvin.turtlematic.common.configuration

import site.siredvin.lib.LibConfig.build
import site.siredvin.turtlematic.common.configuration.ConfigHandler
import site.siredvin.turtlematic.common.configuration.TurlematicConfig
import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.turtlematic.common.configuration.TurlematicConfig.CommonConfig
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue
import site.siredvin.lib.LibConfig
import site.siredvin.lib.misc.IConfigHandler
import site.siredvin.lib.operations.AutomataCoreTier
import site.siredvin.lib.operations.SimpleFreeOperation
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.operations.SphereOperation

object TurlematicConfig {
    //Defaults
    var defaultChatBoxPrefix: String? = null

    //Restrictions
    var playerDetMaxRange = 0
    var energyDetectorMaxFlow = 0
    var poweredPeripheralMaxEnergyStored = 0
    var nbtStorageMaxSize = 0
    var chunkLoadValidTime = 0

    //Features
    var enableChatBox = false
    var enableMeBridge = false
    var enableRsBridge = false
    var enablePlayerDetector = false
    var enableEnvironmentDetector = false
    var enableChunkyTurtle = false
    var enableDebugMode = false
    var enableEnergyDetector = false
    var enableARGoggles = false
    var enableInventoryManager = false
    var enableRedstoneIntegrator = false
    var enableBlockReader = false
    var enableColonyIntegrator = false
    var enableNBTStorage = false
    var enablePoweredPeripherals = false
    var enableGeoScanner = false

    // automata cores configuration
    var energyToFuelRate = 0
    var enableWeakAutomataCore = false
    var enableEndAutomataCore = false
    var enableHusbandryAutomataCore = false
    var endAutomataCoreWarpPointLimit = 0
    var overpoweredAutomataBreakChance = 0.0

    //World Features
    var enableVillagerStructures = false
    var givePlayerBookOnJoin = false

    class CommonConfig internal constructor(builder: ForgeConfigSpec.Builder) {
        //Defaults
        val DEFAULT_CHAT_BOX_PREFIX: ConfigValue<String?>

        //Restrictions
        val PLAYER_DET_MAX_RANGE: ForgeConfigSpec.IntValue
        val ENERGY_DETECTOR_MAX_FLOW: ForgeConfigSpec.IntValue
        val POWERED_PERIPHERAL_MAX_ENERGY_STORED: ForgeConfigSpec.IntValue
        val NBT_STORAGE_MAX_SIZE: ForgeConfigSpec.IntValue
        val CHUNK_LOAD_VALID_TIME: ForgeConfigSpec.IntValue

        //Features
        val ENABLE_CHAT_BOX: ForgeConfigSpec.BooleanValue
        val ENABLE_ME_BRIDGE: ForgeConfigSpec.BooleanValue
        val ENABLE_RS_BRIDGE: ForgeConfigSpec.BooleanValue
        val ENABLE_PLAYER_DETECTOR: ForgeConfigSpec.BooleanValue
        val ENABLE_ENVIRONMENT_DETECTOR: ForgeConfigSpec.BooleanValue
        val ENABLE_CHUNKY_TURTLE: ForgeConfigSpec.BooleanValue
        val ENABLE_DEBUG_MODE: ForgeConfigSpec.BooleanValue
        val ENABLE_ENERGY_DETECTOR: ForgeConfigSpec.BooleanValue
        val ENABLE_AR_GOGGLES: ForgeConfigSpec.BooleanValue
        val ENABLE_INVENTORY_MANAGER: ForgeConfigSpec.BooleanValue
        val ENABLE_REDSTONE_INTEGRATOR: ForgeConfigSpec.BooleanValue
        val ENABLE_BLOCK_READER: ForgeConfigSpec.BooleanValue
        val ENABLE_GEO_SCANNER: ForgeConfigSpec.BooleanValue
        val ENABLE_COLONY_INTEGRATOR: ForgeConfigSpec.BooleanValue
        val ENABLE_NBT_STORAGE: ForgeConfigSpec.BooleanValue
        val ENABLE_POWERED_PERIPHERALS: ForgeConfigSpec.BooleanValue

        // Automata Core
        val ENERGY_TO_FUEL_RATE: ForgeConfigSpec.IntValue
        val ENABLE_WEAK_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_END_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val ENABLE_HUSBANDRY_AUTOMATA_CORE: ForgeConfigSpec.BooleanValue
        val END_AUTOMATA_CORE_WARP_POINT_LIMIT: ForgeConfigSpec.IntValue
        val OVERPOWERED_AUTOMATA_BREAK_CHANCE: ForgeConfigSpec.DoubleValue

        //World Features
        val ENABLE_VILLAGER_STRUCTURES: ForgeConfigSpec.BooleanValue
        val GIVE_PLAYER_BOOK_ON_JOIN: ForgeConfigSpec.BooleanValue

        init {
            builder.comment("").push("Defaults")
            DEFAULT_CHAT_BOX_PREFIX =
                builder.comment("Defines default chatbox prefix").define("defaultChatBoxPrefix", "AP")
            builder.pop()
            builder.push("Core")
            build(builder)
            builder.pop()
            builder.push("Restrictions")
            PLAYER_DET_MAX_RANGE = builder.comment(
                "The max range of the player detector functions. " +
                        "If anyone use a higher range, the detector will use this max range"
            ).defineInRange("playerDetMaxRange", 100000000, 0, 100000000)
            ENERGY_DETECTOR_MAX_FLOW = builder.comment("Defines the maximum energy flow of the energy detector.")
                .defineInRange("energyDetectorMaxFlow", Int.MAX_VALUE, 1, Int.MAX_VALUE)
            POWERED_PERIPHERAL_MAX_ENERGY_STORED =
                builder.comment("Defines max energy stored in any powered peripheral")
                    .defineInRange("poweredPeripheralMaxEnergyStored", 100000000, 1000000, Int.MAX_VALUE)
            NBT_STORAGE_MAX_SIZE = builder.comment("Defines max nbt string that can be stored in nbt storage")
                .defineInRange("nbtStorageMaxSize", 1048576, 0, Int.MAX_VALUE)
            CHUNK_LOAD_VALID_TIME =
                builder.comment("Time in seconds, while loaded chunk can be consider as valid without touch")
                    .defineInRange("chunkLoadValidTime", 600, 60, Int.MAX_VALUE)
            builder.pop()
            builder.push("Features")
            ENABLE_CHAT_BOX = builder.comment("Enable the Chat Box or not.").define("enableChatBox", true)
            ENABLE_ME_BRIDGE = builder.comment("Enable the Me Bridge or not.").define("enableMeBridge", true)
            ENABLE_RS_BRIDGE = builder.comment("Enable the Rs Bridge or not.").define("enableRsBridge", true)
            ENABLE_PLAYER_DETECTOR =
                builder.comment("Enable the Player Detector or not.").define("enablePlayerDetector", true)
            ENABLE_ENVIRONMENT_DETECTOR =
                builder.comment("Enable the Environment Detector or not.").define("enableEnvironmentDetector", true)
            ENABLE_CHUNKY_TURTLE =
                builder.comment("Enable the Chunky Turtle or not.").define("enableChunkyTurtle", true)
            ENABLE_DEBUG_MODE =
                builder.comment("Enable the debug mode. You should only enable it, if a developer say it or something does not work.")
                    .define("enableDebugMode", false)
            ENABLE_ENERGY_DETECTOR =
                builder.comment("Enable the Energy Detector or not.").define("enableEnergyDetector", true)
            ENABLE_AR_GOGGLES = builder.comment("Enable the AR goggles or not.").define("enableARGoggles", true)
            ENABLE_INVENTORY_MANAGER =
                builder.comment("Enable the inventory manager or not.").define("enableInventoryManager", true)
            ENABLE_REDSTONE_INTEGRATOR =
                builder.comment("Enable the redstone integrator or not.").define("enableRedstoneIntegrator", true)
            ENABLE_BLOCK_READER = builder.comment("Enable the block reader or not.").define("enableBlockReader", true)
            ENABLE_GEO_SCANNER = builder.comment("Enable the geo scanner or not.").define("enableGeoScanner", true)
            ENABLE_COLONY_INTEGRATOR =
                builder.comment("Enable the colony integrator or not.").define("enableColonyIntegrator", true)
            ENABLE_NBT_STORAGE = builder.comment("Enable the nbt storage block or not").define("enableNBTStorage", true)
            ENABLE_POWERED_PERIPHERALS = builder.comment("Enable RF storage for peripherals, that could use it")
                .define("enablePoweredPeripherals", false)
            builder.pop()
            builder.push("operations")
            register(SingleOperation.values(), builder)
            register(SphereOperation.values(), builder)
            register(SimpleFreeOperation.values(), builder)
            builder.pop()
            builder.push("metaphysics")
            ENERGY_TO_FUEL_RATE = builder.comment("Defines energy to fuel rate")
                .defineInRange("energyToFuelRate", 575, 575, Int.MAX_VALUE)
            ENABLE_WEAK_AUTOMATA_CORE = builder.define("enableWeakAutomataCore", true)
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
            builder.push("world")
            ENABLE_VILLAGER_STRUCTURES = builder.comment("Enable the villager structures for the computer scientist.")
                .define("enableVillagerStructures", true)
            GIVE_PLAYER_BOOK_ON_JOIN = builder.comment("Gives the ap documentation to new players on a world.")
                .define("givePlayerBookOnJoin", true)
            builder.pop()
        }

        protected fun register(data: Array<out IConfigHandler>, builder: ForgeConfigSpec.Builder) {
            for (handler in data) {
                handler.addToConfig(builder)
            }
        }
    }
}