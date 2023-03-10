package site.siredvin.turtlematic.computercraft

import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.turtle.ITurtleUpgrade
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.api.TurtleIDBuildFunction
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.computercraft.turtle.FacingBlockTurtle
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.peripheralium.util.ItemUtil
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.automatas.*
import site.siredvin.turtlematic.computercraft.peripheral.forged.BrewingAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.forged.EnchantingAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.forged.MasonAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.forged.SmithingAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.misc.*
import site.siredvin.turtlematic.computercraft.turtle.BlockTurtleUpgrade
import site.siredvin.turtlematic.computercraft.turtle.ClockwiseAnimatedTurtleUpgrade
import site.siredvin.turtlematic.computercraft.turtle.StarboundTurtleUpgrade
import site.siredvin.turtlematic.computercraft.turtle.TickerFunctions

object ComputerCraftProxy {

    private val TURTLE_UPGRADES = mutableListOf<ITurtleUpgrade>()
    private val WITH_TURTLEMATIC_ID = TurtleIDBuildFunction {
        val base = Registry.ITEM.getKey(it)
        return@TurtleIDBuildFunction ResourceLocation(Turtlematic.MOD_ID, base.path)
    }

    var CHATTER_TURTLE: ITurtleUpgrade? = null

    fun initialize() {
        CHATTER_TURTLE = BlockTurtleUpgrade.dynamic(Items.TURTLE_CHATTER) { turtle, side -> TurtleChatterPeripheral(TurtlePeripheralOwner(turtle, side)) }

        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.AUTOMATA_CORE) { turtle, side, tier -> AutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.HUSBANDRY_AUTOMATA_CORE) { turtle, side, tier -> HusbandryAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.MERCANTILE_AUTOMATA_CORE) { turtle, side, tier -> MercantileAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.END_AUTOMATA_CORE) { turtle, side, tier -> EndAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.ticker(
            Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE,
            { turtle, side, tier -> HusbandryAutomataCorePeripheral(turtle, side, tier) },
            TickerFunctions::netheriteHusbandryTick
        ))
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.NETHERITE_MERCANTILE_AUTOMATA_CORE) { turtle, side, tier -> MercantileAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.NETHERITE_END_AUTOMATA_CORE) { turtle, side, tier -> EndAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.ticker(
            Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE,
            { turtle, side, tier -> HusbandryAutomataCorePeripheral(turtle, side, tier) },
            TickerFunctions::starboundHusbandryTick
        ))
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_MERCANTILE_AUTOMATA_CORE) { turtle, side, tier -> MercantileAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_END_AUTOMATA_CORE) { turtle, side, tier -> EndAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.ticker(
            Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE,
            { turtle, side, tier -> HusbandryAutomataCorePeripheral(turtle, side, tier) },
            TickerFunctions::creativeHusbandryTick
        ))
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_MERCANTILE_AUTOMATA_CORE) { turtle, side, tier -> MercantileAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_END_AUTOMATA_CORE) { turtle, side, tier -> EndAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.ENORMOUS_AUTOMATA_CORE) { turtle, side, tier -> EnormousAutomataCorePeripheral(turtle, side, tier) })

        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.BREWING_AUTOMATA_CORE) { turtle, side, tier -> BrewingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.MASON_AUTOMATA_CORE) { turtle, side, tier -> MasonAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.ENCHANTING_AUTOMATA_CORE) { turtle, side, tier -> EnchantingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.SMITHING_AUTOMATA_CORE) { turtle, side, tier -> SmithingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_BREWING_AUTOMATA_CORE) { turtle, side, tier -> BrewingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_MASON_AUTOMATA_CORE) { turtle, side, tier -> MasonAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_ENCHANTING_AUTOMATA_CORE) { turtle, side, tier -> EnchantingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(StarboundTurtleUpgrade.dynamic(Items.STARBOUND_SMITHING_AUTOMATA_CORE) { turtle, side, tier -> SmithingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_BREWING_AUTOMATA_CORE) { turtle, side, tier -> BrewingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_MASON_AUTOMATA_CORE) { turtle, side, tier -> MasonAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_ENCHANTING_AUTOMATA_CORE) { turtle, side, tier -> EnchantingAutomataCorePeripheral(turtle, side, tier) })
        TURTLE_UPGRADES.add(ClockwiseAnimatedTurtleUpgrade.dynamic(Items.CREATIVE_SMITHING_AUTOMATA_CORE) { turtle, side, tier -> SmithingAutomataCorePeripheral(turtle, side, tier) })

        TURTLE_UPGRADES.add(PeripheralTurtleUpgrade.dynamic(Items.SOUL_SCRAPPER) { turtle, side -> SoulScrapperPeripheral(TurtlePeripheralOwner(turtle, side)) })
        TURTLE_UPGRADES.add(PeripheralTurtleUpgrade.dynamic(net.minecraft.world.item.Items.LAVA_BUCKET, { turtle, side -> LavaBucketPeripheral(TurtlePeripheralOwner(turtle, side)) }, WITH_TURTLEMATIC_ID))
        TURTLE_UPGRADES.add(CHATTER_TURTLE!!)
        TURTLE_UPGRADES.add(BlockTurtleUpgrade.dynamic(Items.CREATIVE_CHEST) { turtle, side -> CreativeChestPeripheral(TurtlePeripheralOwner(turtle, side)) })

        TURTLE_UPGRADES.add(
            FacingBlockTurtle.dynamic(
                net.minecraft.world.item.Items.PISTON,
                { ResourceLocation(Turtlematic.MOD_ID, Registry.ITEM.getKey(it).path) },
                { turtle, side -> PistonPeripheral(TurtlePeripheralOwner(turtle, side)) }
            )
        )
        TURTLE_UPGRADES.add(
            FacingBlockTurtle.dynamic(
                net.minecraft.world.item.Items.STICKY_PISTON,
                { ResourceLocation(Turtlematic.MOD_ID, Registry.ITEM.getKey(it).path) },
                { turtle, side -> StickyPistonPeripheral(TurtlePeripheralOwner(turtle, side)) }
            )
        )

        TURTLE_UPGRADES.forEach {
            ComputerCraftAPI.registerTurtleUpgrade(it)
        }
    }

    fun fillCreativeTab(stacksToApply: MutableList<ItemStack>) {
        TURTLE_UPGRADES.forEach {
            stacksToApply.add(ItemUtil.makeTurtle(it.upgradeID.toString()))
            stacksToApply.add(ItemUtil.makeAdvancedTurtle(it.upgradeID.toString()))
        }
    }
}