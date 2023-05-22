package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.client.ComputerCraftAPIClient
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.api.TurtlePeripheralBuildFunction
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import site.siredvin.peripheralium.client.FacingBlockTurtleModeller
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.peripheralium.xplat.PeripheraliumPlatform
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.TurtlematicCoreClient
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.api.AutomataTickerFunction
import site.siredvin.turtlematic.client.AngleItemTurtleModeller
import site.siredvin.turtlematic.client.ClockwiseTurtleModeller
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.setup.EntityTypes
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.automatas.AutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.EndAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.EnormousAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.HusbandryAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.forged.*
import site.siredvin.turtlematic.computercraft.peripheral.misc.*
import site.siredvin.turtlematic.computercraft.turtle.ChunkVialTurtle
import site.siredvin.turtlematic.computercraft.turtle.ClockwiseTurtleUpgrade
import site.siredvin.turtlematic.computercraft.turtle.StarboundTurtleUpgrade
import site.siredvin.turtlematic.computercraft.turtle.TickerFunctions
import site.siredvin.turtlematic.util.ChunkManager
import site.siredvin.turtlematic.util.toCreative
import site.siredvin.turtlematic.util.toNetherite
import site.siredvin.turtlematic.util.toStarbound
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier

object TurtlematicCommonHooks {

    private val TURTLE_UPGRADES: MutableList<ResourceLocation> = mutableListOf()

    private fun <T: IOwnedPeripheral<TurtlePeripheralOwner>, V: Item> registerSimpleTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: TurtlePeripheralBuildFunction<T>) {
        registerSimpleTurtleUpgrade(id, item, BiFunction<ResourceLocation, ItemStack, PeripheralTurtleUpgrade<*>> { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, builder) { upgradeID }
        })
    }

    private fun <T: ITurtleUpgrade, V: Item> registerSimpleTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder:  BiFunction<ResourceLocation, ItemStack, T>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem { upgradeID, stack -> builder.apply(upgradeID, stack) },
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), TurtleUpgradeModeller.sided(
                            ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${id.path}_left"),
                            ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${id.path}_right")
                        )
                    )
                }
            })
        )
    }

    private fun <T: IOwnedPeripheral<TurtlePeripheralOwner>, V: Item> registerFlatTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: TurtlePeripheralBuildFunction<T>) {
        registerFlatTurtleUpgrade(id, item, BiFunction<ResourceLocation, ItemStack, PeripheralTurtleUpgrade<*>> { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, builder) { upgradeID }
        })
    }

    private fun <T: ITurtleUpgrade, V: Item> registerFlatTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: BiFunction<ResourceLocation, ItemStack, T>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem(builder::apply),
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), TurtleUpgradeModeller.flatItem()
                    )
                }
            })
        )
    }

    private fun <T: IOwnedPeripheral<TurtlePeripheralOwner>, V: Item> registerFacingBlockTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: TurtlePeripheralBuildFunction<T>) {
        registerFacingBlockTurtleUpgrade(id, item, BiFunction<ResourceLocation, ItemStack, PeripheralTurtleUpgrade<*>> { upgradeID, stack ->
            PeripheralTurtleUpgrade.dynamic(stack.item, builder) { upgradeID }
        })
    }


    private fun <T: ITurtleUpgrade, V: Item> registerFacingBlockTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: BiFunction<ResourceLocation, ItemStack, T>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem(builder::apply),
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), FacingBlockTurtleModeller()
                    )
                }
            })
        )
    }

    private fun <O: IOwnedPeripheral<*>, V: Item> registerClockwiseTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: AutomataPeripheralBuildFunction<O>, ticker: AutomataTickerFunction? = null) {
        registerClockwiseTurtleUpgrade(id, item) { upgradeId, stack ->
            if (ticker == null) {
                ClockwiseTurtleUpgrade.dynamic(upgradeId, stack.item as BaseAutomataCore, builder)}
            else {
                ClockwiseTurtleUpgrade.ticker(upgradeId, stack.item as BaseAutomataCore, builder, ticker)
            }
        }
    }

    private fun <T: IOwnedPeripheral<TurtlePeripheralOwner>, V: Item> registerRenderedTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: TurtlePeripheralBuildFunction<T>, renderer: Supplier<TurtleUpgradeModeller<PeripheralTurtleUpgrade<T>>>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem{upgradeID, stack ->
                PeripheralTurtleUpgrade.dynamic(stack.item, builder) { upgradeID }
            },
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), renderer.get()
                    )
                }
            })
        )
    }

    private fun <T: ITurtleUpgrade, V: Item> registerClockwiseTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: BiFunction<ResourceLocation, ItemStack, T>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem(builder::apply),
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), ClockwiseTurtleModeller()
                    )
                }
            })
        )
    }

    private fun <O: IOwnedPeripheral<*>, V: Item> registerStarboundTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: AutomataPeripheralBuildFunction<O>, ticker: AutomataTickerFunction? = null) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem {  upgradeId, stack ->
                if (ticker == null) {StarboundTurtleUpgrade.dynamic(upgradeId, stack.item as BaseAutomataCore, builder)} else {
                    StarboundTurtleUpgrade.ticker(upgradeId, stack.item as BaseAutomataCore, builder, ticker)
                }  },
            { dataProvider, serializer ->
                dataProvider.simpleWithCustomItem(
                    id,
                    serializer, item.get()
                )
            } ,
            listOf(Consumer {
                TurtlematicCoreClient.registerHook {
                    ComputerCraftAPIClient.registerTurtleUpgradeModeller(
                        it.get(), ClockwiseTurtleModeller()
                    )
                }
            })
        )
    }

    private fun registerTurtleUpgrades() {
        registerClockwiseTurtleUpgrade(AutomataCorePeripheral.UPGRADE_ID, Items.AUTOMATA_CORE, ::AutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.UPGRADE_ID, Items.HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.UPGRADE_ID.toNetherite(), Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral, TickerFunctions::netheriteHusbandryTick)
        registerStarboundTurtleUpgrade(HusbandryAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral, TickerFunctions::starboundHusbandryTick)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral, TickerFunctions::creativeHusbandryTick)
        registerClockwiseTurtleUpgrade(EndAutomataCorePeripheral.UPGRADE_ID, Items.END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(EndAutomataCorePeripheral.UPGRADE_ID.toNetherite(), Items.NETHERITE_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(EndAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(EndAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(EnormousAutomataCorePeripheral.UPGRADE_ID, Items.ENORMOUS_AUTOMATA_CORE, ::EnormousAutomataCorePeripheral)

        registerClockwiseTurtleUpgrade(BrewingAutomataCorePeripheral.UPGRADE_ID, Items.BREWING_AUTOMATA_CORE, ::BrewingAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(BrewingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_BREWING_AUTOMATA_CORE, ::BrewingAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(BrewingAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_BREWING_AUTOMATA_CORE, ::BrewingAutomataCorePeripheral)

        registerClockwiseTurtleUpgrade(EnchantingAutomataCorePeripheral.UPGRADE_ID, Items.ENCHANTING_AUTOMATA_CORE, ::EnchantingAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(EnchantingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_ENCHANTING_AUTOMATA_CORE, ::EnchantingAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(EnchantingAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_ENCHANTING_AUTOMATA_CORE, ::EnchantingAutomataCorePeripheral)

        registerClockwiseTurtleUpgrade(MasonAutomataCorePeripheral.UPGRADE_ID, Items.MASON_AUTOMATA_CORE, ::MasonAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(MasonAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_MASON_AUTOMATA_CORE, ::MasonAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(MasonAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_MASON_AUTOMATA_CORE, ::MasonAutomataCorePeripheral)

        registerClockwiseTurtleUpgrade(MercantileAutomataCorePeripheral.UPGRADE_ID, Items.MERCANTILE_AUTOMATA_CORE, ::MercantileAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(MercantileAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_MERCANTILE_AUTOMATA_CORE, ::MercantileAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(MercantileAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_MERCANTILE_AUTOMATA_CORE, ::MercantileAutomataCorePeripheral)

        registerClockwiseTurtleUpgrade(SmithingAutomataCorePeripheral.UPGRADE_ID, Items.SMITHING_AUTOMATA_CORE, ::SmithingAutomataCorePeripheral)
        registerStarboundTurtleUpgrade(SmithingAutomataCorePeripheral.UPGRADE_ID.toStarbound(), Items.STARBOUND_SMITHING_AUTOMATA_CORE, ::SmithingAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(SmithingAutomataCorePeripheral.UPGRADE_ID.toCreative(), Items.CREATIVE_SMITHING_AUTOMATA_CORE, ::SmithingAutomataCorePeripheral)

        registerSimpleTurtleUpgrade(TurtleChatterPeripheral.UPGRADE_ID, Items.TURTLE_CHATTER, ::TurtleChatterPeripheral)
        registerSimpleTurtleUpgrade(CreativeChestPeripheral.UPGRADE_ID, Items.CREATIVE_CHEST, ::CreativeChestPeripheral)
        registerSimpleTurtleUpgrade(ChunkVialPeripheral.UPGRADE_ID, Items.CHUNK_VIAL, ::ChunkVialTurtle)

        registerFlatTurtleUpgrade(SoulScrapperPeripheral.UPGRADE_ID, Items.SOUL_SCRAPPER, ::SoulScrapperPeripheral)

        registerFlatTurtleUpgrade(LavaBucketPeripheral.UPGRADE_ID, { net.minecraft.world.item.Items.LAVA_BUCKET }, ::LavaBucketPeripheral)
        registerRenderedTurtleUpgrade(BowPeripheral.UPGRADE_ID, { net.minecraft.world.item.Items.BOW }, ::BowPeripheral, ::AngleItemTurtleModeller)


        registerFacingBlockTurtleUpgrade(PistonPeripheral.UPGRADE_ID, { net.minecraft.world.item.Items.PISTON }, ::PistonPeripheral)
        registerFacingBlockTurtleUpgrade(StickyPistonPeripheral.UPGRADE_ID, { net.minecraft.world.item.Items.STICKY_PISTON }, ::StickyPistonPeripheral)
    }

    fun onRegister() {
        Items.doSomething()
        EntityTypes.doSomething()
        registerTurtleUpgrades()
    }

    fun commonSetup() {
        SoulHarvestRecipeRegistry.injectAutomataCoreRecipes()
        SoulHarvestRecipeRegistry.injectForgedAutomataCoreRecipes()
    }

    fun registerTurtlesInCreativeTab(output: CreativeModeTab.Output) {
        TURTLE_UPGRADES.forEach {
            val upgrade = PeripheraliumPlatform.getTurtleUpgrade(it.toString())
            if (upgrade != null)
                PeripheraliumPlatform.createTurtlesWithUpgrade(upgrade).forEach(output::accept)
        }
    }

    fun onServerStarted(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).init(server)
    }

    fun onServerStopping(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).stop(server)
    }

    fun onEndOfServerTick(server: MinecraftServer) {
        ChunkManager.get(server.overworld()).tick(server)
    }
}