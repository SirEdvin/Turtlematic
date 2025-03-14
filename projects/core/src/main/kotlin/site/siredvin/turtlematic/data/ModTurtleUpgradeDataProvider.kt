package site.siredvin.turtlematic.data

import dan200.computercraft.api.turtle.ITurtleUpgrade
import net.minecraft.Util
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.RegistrySetBuilder.PatchedRegistries
import net.minecraft.data.registries.RegistryPatchGenerator
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import site.siredvin.broccolium.modules.platform.api.RegistryEntry
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.common.setup.TurtleUpgradeSerializers
import site.siredvin.tweakium.modules.platform.ReducedTurtleUpgradeTypeRegistryEntry
import site.siredvin.tweakium.modules.platform.TurtleUpgradeTypeRegistryEntry
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

object ModTurtleUpgradeDataProvider {
    private val REGISTERED_BUILDERS: MutableList<Consumer<BootstrapContext<ITurtleUpgrade>>> = mutableListOf()

    fun hookUpgrade(builder: Consumer<BootstrapContext<ITurtleUpgrade>>) {
        REGISTERED_BUILDERS.add(builder)
    }

    fun registerUpgrade(upgrades: BootstrapContext<ITurtleUpgrade>, upgrade: TurtleUpgradeTypeRegistryEntry<out ITurtleUpgrade>, item: RegistryEntry<out Item>) {
        upgrades.register(
            ResourceKey.create(ITurtleUpgrade.REGISTRY, upgrade.id),
            upgrade.createUpgrade(item.get().defaultInstance),
        )
    }

    fun registerUpgrade(upgrades: BootstrapContext<ITurtleUpgrade>, upgrade: ReducedTurtleUpgradeTypeRegistryEntry<out ITurtleUpgrade>, item: RegistryEntry<out Item>) {
        upgrades.register(
            ResourceKey.create(ITurtleUpgrade.REGISTRY, upgrade.id),
            upgrade.createUpgrade(item.get().defaultInstance),
        )
    }

    fun registerUpgrade(upgrades: BootstrapContext<ITurtleUpgrade>, upgrade: TurtleUpgradeTypeRegistryEntry<out ITurtleUpgrade>, item: Item) {
        upgrades.register(
            ResourceKey.create(ITurtleUpgrade.REGISTRY, upgrade.id),
            upgrade.createUpgrade(item.defaultInstance),
        )
    }

    fun addUpgrades(upgrades: BootstrapContext<ITurtleUpgrade>) {
        REGISTERED_BUILDERS.forEach { it.accept(upgrades) }

        registerUpgrade(upgrades, TurtleUpgradeSerializers.TURTLE_CHATTER, Items.TURTLE_CHATTER)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.MIMIC, Items.MIMIC_GADGET)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_CHEST, Items.CREATIVE_CHEST)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CHUNK_VIAL, Items.CHUNK_VIAL)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.SOUL_SCRAPPER, Items.SOUL_SCRAPPER)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.LAVA_BUCKET, net.minecraft.world.item.Items.LAVA_BUCKET)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.BOW, net.minecraft.world.item.Items.BOW)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.PISTON, net.minecraft.world.item.Items.PISTON)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STICKY_PISTON, net.minecraft.world.item.Items.STICKY_PISTON)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.AUTOMATA_CORE, Items.AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.ENORMOUS_AUTOMATA, Items.ENORMOUS_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.HUSBANDRY_AUTOMATA, Items.HUSBANDRY_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.NETHERITE_HUSBANDRY_AUTOMATA, Items.NETHERITE_HUSBANDRY_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_HUSBANDRY_AUTOMATA, Items.STARBOUND_HUSBANDRY_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_HUSBANDRY_AUTOMATA, Items.CREATIVE_HUSBANDRY_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.END_AUTOMATA, Items.END_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.NETHERITE_END_AUTOMATA, Items.NETHERITE_END_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_END_AUTOMATA, Items.STARBOUND_END_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_END_AUTOMATA, Items.CREATIVE_END_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.PROTECTIVE_AUTOMATA, Items.PROTECTIVE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.NETHERITE_PROTECTIVE_AUTOMATA, Items.NETHERITE_PROTECTIVE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_PROTECTIVE_AUTOMATA, Items.STARBOUND_PROTECTIVE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_PROTECTIVE_AUTOMATA, Items.CREATIVE_PROTECTIVE_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.BREWING_AUTOMATA, Items.BREWING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.ENCHANTING_AUTOMATA, Items.ENCHANTING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.MASON_AUTOMATA, Items.MASON_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.MERCANTILE_AUTOMATA, Items.MERCANTILE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.SMITHING_AUTOMATA, Items.SMITHING_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_BREWING_AUTOMATA, Items.STARBOUND_BREWING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_ENCHANTING_AUTOMATA, Items.STARBOUND_ENCHANTING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_MASON_AUTOMATA, Items.STARBOUND_MASON_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_MERCANTILE_AUTOMATA, Items.STARBOUND_MERCANTILE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.STARBOUND_SMITHING_AUTOMATA, Items.STARBOUND_SMITHING_AUTOMATA_CORE)

        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_BREWING_AUTOMATA, Items.CREATIVE_BREWING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_ENCHANTING_AUTOMATA, Items.CREATIVE_ENCHANTING_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_MASON_AUTOMATA, Items.CREATIVE_MASON_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_MERCANTILE_AUTOMATA, Items.CREATIVE_MERCANTILE_AUTOMATA_CORE)
        registerUpgrade(upgrades, TurtleUpgradeSerializers.CREATIVE_SMITHING_AUTOMATA, Items.CREATIVE_SMITHING_AUTOMATA_CORE)
    }

    fun makeUpgradeRegistry(registries: CompletableFuture<HolderLookup.Provider>): CompletableFuture<PatchedRegistries> = RegistryPatchGenerator.createLookup(
        registries,
        Util.make(RegistrySetBuilder()) { builder ->
            builder.add(ITurtleUpgrade.REGISTRY, ::addUpgrades)
        },
    )
}
