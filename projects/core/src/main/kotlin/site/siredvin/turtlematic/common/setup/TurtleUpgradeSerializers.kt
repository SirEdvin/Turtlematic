package site.siredvin.turtlematic.common.setup

import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.computercraft.peripheral.automatas.*
import site.siredvin.turtlematic.computercraft.peripheral.forged.*
import site.siredvin.turtlematic.computercraft.peripheral.misc.*
import site.siredvin.turtlematic.computercraft.turtle.*
import site.siredvin.turtlematic.util.toCreative
import site.siredvin.turtlematic.util.toNetherite
import site.siredvin.turtlematic.util.toStarbound
import site.siredvin.turtlematic.xplat.ModPlatform
import site.siredvin.tweakium.modules.turtle.PeripheralTurtleUpgrade

object TurtleUpgradeSerializers {

    val TURTLE_CHATTER = ModPlatform.registerTurtleUpgrade(
        TurtleChatterPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::TurtleChatterPeripheral) { upgradeID }
        },
    )

    val MIMIC = ModPlatform.registerTurtleUpgrade(
        MimicPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::MimicPeripheral) { upgradeID }
        },
    )

    val CREATIVE_CHEST = ModPlatform.registerTurtleUpgrade(
        CreativeChestPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::CreativeChestPeripheral) { upgradeID }
        },
    )

    val CHUNK_VIAL = ModPlatform.registerTurtleUpgrade(
        ChunkVialPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem(::ChunkVialTurtle),
    )

    val INSPECTION_MONOCLE = ModPlatform.registerTurtleUpgrade(
        InspectionMonoclePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::InspectionMonoclePeripheral) { upgradeID }
        },
    )

    val SOUL_SCRAPPER = ModPlatform.registerTurtleUpgrade(
        SoulScrapperPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::SoulScrapperPeripheral) { upgradeID }
        },
    )

    val LAVA_BUCKET = ModPlatform.registerTurtleUpgrade(
        LavaBucketPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::LavaBucketPeripheral) { upgradeID }
        },
    )

    val BOW = ModPlatform.registerTurtleUpgrade(
        BowPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::BowPeripheral) { upgradeID }
        },
    )

    val PISTON = ModPlatform.registerTurtleUpgrade(
        PistonPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::PistonPeripheral) { upgradeID }
        },
    )

    val STICKY_PISTON = ModPlatform.registerTurtleUpgrade(
        StickyPistonPeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::StickyPistonPeripheral) { upgradeID }
        },
    )

    val AUTOMATA_CORE = ModPlatform.registerTurtleUpgrade(
        AutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::AutomataCorePeripheral)
        },
    )

    val HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::HusbandryAutomataCorePeripheral)
        },
    )

    val NETHERITE_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.upgradeID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::HusbandryAutomataCorePeripheral)
        },
    )

    val STARBOUND_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::HusbandryAutomataCorePeripheral)
        },
    )

    val CREATIVE_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        HusbandryAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::HusbandryAutomataCorePeripheral)
        },
    )

    val END_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val NETHERITE_END_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.upgradeID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val STARBOUND_END_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val CREATIVE_END_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EndAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EndAutomataCorePeripheral)
        },
    )

    val PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val NETHERITE_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.upgradeID.toNetherite(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val STARBOUND_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val CREATIVE_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        ProtectiveAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val ENORMOUS_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EnormousAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnormousAutomataCorePeripheral)
        },
    )

    val BREWING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val MASON_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.upgradeID,
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    val STARBOUND_BREWING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val STARBOUND_ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val STARBOUND_MASON_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val STARBOUND_MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val STARBOUND_SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.upgradeID.toStarbound(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    val CREATIVE_BREWING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        BrewingAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::BrewingAutomataCorePeripheral)
        },
    )

    val CREATIVE_ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        EnchantingAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::EnchantingAutomataCorePeripheral)
        },
    )

    val CREATIVE_MASON_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MasonAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MasonAutomataCorePeripheral)
        },
    )

    val CREATIVE_MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        MercantileAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::MercantileAutomataCorePeripheral)
        },
    )

    val CREATIVE_SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgrade(
        SmithingAutomataCorePeripheral.upgradeID.toCreative(),
        TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeId, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@simpleWithCustomItem DisabledTurtleUpgrade(upgradeId, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, ::SmithingAutomataCorePeripheral)
        },
    )

    fun doSomething() {}
}
