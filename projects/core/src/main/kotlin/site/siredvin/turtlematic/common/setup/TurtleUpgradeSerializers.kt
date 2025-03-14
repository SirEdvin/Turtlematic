package site.siredvin.turtlematic.common.setup

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

    val TURTLE_CHATTER = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        TurtleChatterPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::TurtleChatterPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val MIMIC = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MimicPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::MimicPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val CREATIVE_CHEST = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        CreativeChestPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::CreativeChestPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val CHUNK_VIAL = ModPlatform.registerTurtleUpgradeWithCustomItem(
        ChunkVialPeripheral.upgradeID,
        ::ChunkVialTurtle,
    )

    val SOUL_SCRAPPER = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        SoulScrapperPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::SoulScrapperPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val LAVA_BUCKET = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        LavaBucketPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::LavaBucketPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val BOW = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        BowPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::BowPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val PISTON = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        PistonPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::PistonPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val STICKY_PISTON = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        StickyPistonPeripheral.upgradeID,
        { upgradeID, upgradeType, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, ::StickyPistonPeripheral, { upgradeType }) { upgradeID }
        },
    )

    val AUTOMATA_CORE = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        AutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::AutomataCorePeripheral)
        },
    )

    val HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        HusbandryAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::HusbandryAutomataCorePeripheral)
        },
    )

    val NETHERITE_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        HusbandryAutomataCorePeripheral.upgradeID.toNetherite(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::HusbandryAutomataCorePeripheral)
        },
    )

    val STARBOUND_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        HusbandryAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::HusbandryAutomataCorePeripheral)
        },
    )

    val CREATIVE_HUSBANDRY_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        HusbandryAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::HusbandryAutomataCorePeripheral)
        },
    )

    val END_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EndAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EndAutomataCorePeripheral)
        },
    )

    val NETHERITE_END_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EndAutomataCorePeripheral.upgradeID.toNetherite(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EndAutomataCorePeripheral)
        },
    )

    val STARBOUND_END_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EndAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EndAutomataCorePeripheral)
        },
    )

    val CREATIVE_END_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EndAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EndAutomataCorePeripheral)
        },
    )

    val PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        ProtectiveAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val NETHERITE_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        ProtectiveAutomataCorePeripheral.upgradeID.toNetherite(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val STARBOUND_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        ProtectiveAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val CREATIVE_PROTECTIVE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        ProtectiveAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::ProtectiveAutomataCorePeripheral)
        },
    )

    val ENORMOUS_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EnormousAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EnormousAutomataCorePeripheral)
        },
    )

    val BREWING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        BrewingAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::BrewingAutomataCorePeripheral)
        },
    )

    val ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EnchantingAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EnchantingAutomataCorePeripheral)
        },
    )

    val MASON_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MasonAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MasonAutomataCorePeripheral)
        },
    )

    val MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MercantileAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MercantileAutomataCorePeripheral)
        },
    )

    val SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        SmithingAutomataCorePeripheral.upgradeID,
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::SmithingAutomataCorePeripheral)
        },
    )

    val STARBOUND_BREWING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        BrewingAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::BrewingAutomataCorePeripheral)
        },
    )

    val STARBOUND_ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EnchantingAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EnchantingAutomataCorePeripheral)
        },
    )

    val STARBOUND_MASON_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MasonAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MasonAutomataCorePeripheral)
        },
    )

    val STARBOUND_MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MercantileAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MercantileAutomataCorePeripheral)
        },
    )

    val STARBOUND_SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        SmithingAutomataCorePeripheral.upgradeID.toStarbound(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            StarboundTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::SmithingAutomataCorePeripheral)
        },
    )

    val CREATIVE_BREWING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        BrewingAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::BrewingAutomataCorePeripheral)
        },
    )

    val CREATIVE_ENCHANTING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        EnchantingAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::EnchantingAutomataCorePeripheral)
        },
    )

    val CREATIVE_MASON_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MasonAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MasonAutomataCorePeripheral)
        },
    )

    val CREATIVE_MERCANTILE_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        MercantileAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::MercantileAutomataCorePeripheral)
        },
    )

    val CREATIVE_SMITHING_AUTOMATA = ModPlatform.registerTurtleUpgradeWithSelfCustomItem(
        SmithingAutomataCorePeripheral.upgradeID.toCreative(),
        { upgradeId, upgradeType, stack ->
            val core = stack.item as? BaseAutomataCore ?: return@registerTurtleUpgradeWithSelfCustomItem DisabledTurtleUpgrade(upgradeId, upgradeType, stack)
            ClockwiseTurtleUpgrade.dynamic(upgradeId, core, upgradeType, ::SmithingAutomataCorePeripheral)
        },
    )

    fun doSomething() {}
}
