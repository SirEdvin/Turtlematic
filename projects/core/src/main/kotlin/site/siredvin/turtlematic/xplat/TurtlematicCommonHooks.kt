package site.siredvin.turtlematic.xplat

import dan200.computercraft.api.client.ComputerCraftAPIClient
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.TurtlematicCoreClient
import site.siredvin.turtlematic.api.AutomataPeripheralBuildFunction
import site.siredvin.turtlematic.client.ClockwiseTurtleModeller
import site.siredvin.turtlematic.common.items.base.BaseAutomataCore
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.computercraft.peripheral.automatas.AutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.EndAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.EnormousAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.peripheral.automatas.HusbandryAutomataCorePeripheral
import site.siredvin.turtlematic.computercraft.turtle.ClockwiseAnimatedTurtleUpgrade
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

object TurtlematicCommonHooks {

    private val TURTLE_UPGRADES: MutableList<ResourceLocation> = mutableListOf()

    private fun <T: ITurtleUpgrade, V: Item> registerSimpleTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: BiFunction<ItemStack, ResourceLocation, T>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem { _, stack -> builder.apply(stack, id) },
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

    private fun <O: IOwnedPeripheral<*>, T: ClockwiseAnimatedTurtleUpgrade<O>, V: Item> registerClockwiseTurtleUpgrade(id: ResourceLocation, item: Supplier<V>, builder: AutomataPeripheralBuildFunction<O>) {
        TURTLE_UPGRADES.add(id)
        TurtlematicPlatform.registerTurtleUpgrade(
            id,
            TurtleUpgradeSerialiser.simpleWithCustomItem { _, stack -> ClockwiseAnimatedTurtleUpgrade.dynamic(stack.item as BaseAutomataCore, builder) },
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
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.NETHERITE_UPGRADE_ID, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.STARBOUND_UPGRADE_ID, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.CREATIVE_UPGRADE_ID, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE, ::HusbandryAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.UPGRADE_ID, Items.END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.NETHERITE_UPGRADE_ID, Items.NETHERITE_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.STARBOUND_UPGRADE_ID, Items.STARBOUND_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(HusbandryAutomataCorePeripheral.CREATIVE_UPGRADE_ID, Items.CREATIVE_END_AUTOMATA_CORE, ::EndAutomataCorePeripheral)
        registerClockwiseTurtleUpgrade(EnormousAutomataCorePeripheral.UPGRADE_ID, Items.ENORMOUS_AUTOMATA_CORE, ::EnormousAutomataCorePeripheral)

    }

    fun onRegister() {
        Items.doSomething()
        registerTurtleUpgrades()
    }
}