package site.siredvin.turtlematic.common.setup

import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.computercraft.peripheral.automatas.*
import site.siredvin.turtlematic.computercraft.peripheral.forged.*
import site.siredvin.turtlematic.computercraft.peripheral.misc.*
import site.siredvin.turtlematic.computercraft.turtle.*
import site.siredvin.turtlematic.util.toCreative
import site.siredvin.turtlematic.util.toNetherite
import site.siredvin.turtlematic.util.toStarbound
import site.siredvin.turtlematic.xplat.TurtlematicPlatform

object TurtleUpgradeSerializers {

    val TURTLE_CHATTER = TurtlematicPlatform.registerTurtleUpgrade(
        TurtleChatterPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::TurtleChatterPeripheral) { upgradeID }
        },
    )

    val MIMIC = TurtlematicPlatform.registerTurtleUpgrade(
        MimicPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::MimicPeripheral) { upgradeID }
        },
    )

    val CREATIVE_CHEST = TurtlematicPlatform.registerTurtleUpgrade(
        CreativeChestPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::CreativeChestPeripheral) { upgradeID }
        },
    )

    val CHUNK_VIAL = TurtlematicPlatform.registerTurtleUpgrade(
        ChunkVialPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem(::ChunkVialTurtle),
    )

    val SOUL_SCRAPPER = TurtlematicPlatform.registerTurtleUpgrade(
        SoulScrapperPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::SoulScrapperPeripheral) { upgradeID }
        },
    )

    val LAVA_BUCKET = TurtlematicPlatform.registerTurtleUpgrade(
        LavaBucketPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::LavaBucketPeripheral) { upgradeID }
        },
    )

    val BOW = TurtlematicPlatform.registerTurtleUpgrade(
        BowPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::BowPeripheral) { upgradeID }
        },
    )

    val PISTON = TurtlematicPlatform.registerTurtleUpgrade(
        PistonPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::PistonPeripheral) { upgradeID }
        },
    )

    val STICKY_PISTON = TurtlematicPlatform.registerTurtleUpgrade(
        StickyPistonPeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::StickyPistonPeripheral) { upgradeID }
        },
    )

    val AUTOMATA_CORE = TurtlematicPlatform.registerTurtleUpgrade(
        AutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::AutomataCorePeripheral)
        },
    )

    val HUSBANDRY_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::HusbandryAutomataCorePeripheral)
        },
    )

    val NETHERITE_HUSBANDRY_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.UPGRADE_ID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.ticker(upgradeId, core, ::HusbandryAutomataCorePeripheral, TickerFunctions::netheriteHusbandryTick)
        },
    )

    val STARBOUND_HUSBANDRY_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.ticker(upgradeId, core, ::HusbandryAutomataCorePeripheral, TickerFunctions::starboundHusbandryTick)
        },
    )

    val CREATIVE_HUSBANDRY_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.ticker(upgradeId, core, ::HusbandryAutomataCorePeripheral, TickerFunctions::creativeHusbandryTick)
        },
    )

    val END_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val NETHERITE_END_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.UPGRADE_ID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val STARBOUND_END_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val CREATIVE_END_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val PROTECTIVE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val NETHERITE_PROTECTIVE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.UPGRADE_ID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val STARBOUND_PROTECTIVE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val CREATIVE_PROTECTIVE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val ENORMOUS_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EnormousAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnormousAutomataCorePeripheral)
        },
    )

    val BREWING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val ENCHANTING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val MASON_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val MERCANTILE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val SMITHING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.UPGRADE_ID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    val STARBOUND_BREWING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val STARBOUND_ENCHANTING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val STARBOUND_MASON_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val STARBOUND_MERCANTILE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val STARBOUND_SMITHING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.UPGRADE_ID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    val CREATIVE_BREWING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val CREATIVE_ENCHANTING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val CREATIVE_MASON_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val CREATIVE_MERCANTILE_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val CREATIVE_SMITHING_AUTOMATA = TurtlematicPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.UPGRADE_ID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    fun doSomething() {}
}
