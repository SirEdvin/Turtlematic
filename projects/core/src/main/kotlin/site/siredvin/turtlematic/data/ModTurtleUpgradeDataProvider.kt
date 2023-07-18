package site.siredvin.turtlematic.data

import dan200.computercraft.api.turtle.TurtleUpgradeDataProvider
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.data.PackOutput
import site.siredvin.peripheralium.data.blocks.LibTurtleUpgradeDataProvider
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.common.setup.TurtleUpgradeSerializers
import site.siredvin.turtlematic.xplat.TurtlematicPlatform
import java.util.function.Consumer
import java.util.function.Function

class ModTurtleUpgradeDataProvider(output: PackOutput) : LibTurtleUpgradeDataProvider(output, TurtlematicPlatform.holder.turtleSerializers) {
    companion object {
        private val REGISTERED_BUILDERS: MutableList<Function<TurtleUpgradeDataProvider, Upgrade<TurtleUpgradeSerialiser<*>>>> = mutableListOf()

        fun hookUpgrade(builder: Function<TurtleUpgradeDataProvider, Upgrade<TurtleUpgradeSerialiser<*>>>) {
            REGISTERED_BUILDERS.add(builder)
        }
    }

    override fun registerUpgrades(addUpgrade: Consumer<Upgrade<TurtleUpgradeSerialiser<*>>>) {
        REGISTERED_BUILDERS.forEach {
            it.apply(this).add(addUpgrade)
        }

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.TURTLE_CHATTER, Items.TURTLE_CHATTER))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.MIMIC, Items.MIMIC_GADGET))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_CHEST, Items.CREATIVE_CHEST))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CHUNK_VIAL, Items.CHUNK_VIAL))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.SOUL_SCRAPPER, Items.SOUL_SCRAPPER))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.LAVA_BUCKET, net.minecraft.world.item.Items.LAVA_BUCKET))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.BOW, net.minecraft.world.item.Items.BOW))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.PISTON, net.minecraft.world.item.Items.PISTON))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STICKY_PISTON, net.minecraft.world.item.Items.STICKY_PISTON))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.AUTOMATA_CORE, Items.AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.ENORMOUS_AUTOMATA, Items.ENORMOUS_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.HUSBANDRY_AUTOMATA, Items.HUSBANDRY_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.NETHERITE_HUSBANDRY_AUTOMATA, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_HUSBANDRY_AUTOMATA, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_HUSBANDRY_AUTOMATA, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.END_AUTOMATA, Items.END_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.NETHERITE_END_AUTOMATA, Items.NETHERITE_END_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_END_AUTOMATA, Items.STARBOUND_END_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_END_AUTOMATA, Items.CREATIVE_END_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.PROTECTIVE_AUTOMATA, Items.PROTECTIVE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.NETHERITE_PROTECTIVE_AUTOMATA, Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_PROTECTIVE_AUTOMATA, Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_PROTECTIVE_AUTOMATA, Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.BREWING_AUTOMATA, Items.BREWING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.ENCHANTING_AUTOMATA, Items.ENCHANTING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.MASON_AUTOMATA, Items.MASON_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.MERCANTILE_AUTOMATA, Items.MERCANTILE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.SMITHING_AUTOMATA, Items.SMITHING_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_BREWING_AUTOMATA, Items.STARBOUND_BREWING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_ENCHANTING_AUTOMATA, Items.STARBOUND_ENCHANTING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_MASON_AUTOMATA, Items.STARBOUND_MASON_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_MERCANTILE_AUTOMATA, Items.STARBOUND_MERCANTILE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.STARBOUND_SMITHING_AUTOMATA, Items.STARBOUND_SMITHING_AUTOMATA_CORE))

        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_BREWING_AUTOMATA, Items.CREATIVE_BREWING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_ENCHANTING_AUTOMATA, Items.CREATIVE_ENCHANTING_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_MASON_AUTOMATA, Items.CREATIVE_MASON_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_MERCANTILE_AUTOMATA, Items.CREATIVE_MERCANTILE_AUTOMATA_CORE))
        addUpgrade.accept(simpleWithCustomItem(TurtleUpgradeSerializers.CREATIVE_SMITHING_AUTOMATA, Items.CREATIVE_SMITHING_AUTOMATA_CORE))
    }
}
