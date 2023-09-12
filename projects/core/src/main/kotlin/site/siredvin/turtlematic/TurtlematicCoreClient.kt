package site.siredvin.turtlematic

import dan200.computercraft.api.client.ComputerCraftAPIClient
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller
import dan200.computercraft.api.turtle.ITurtleUpgrade
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import site.siredvin.peripheralium.client.FacingBlockTurtleModeller
import site.siredvin.turtlematic.client.*
import site.siredvin.turtlematic.common.entities.ShootedItemProjectile
import site.siredvin.turtlematic.common.setup.EntityTypes
import site.siredvin.turtlematic.common.setup.TurtleUpgradeSerializers
import site.siredvin.turtlematic.computercraft.peripheral.misc.ChunkVialPeripheral
import site.siredvin.turtlematic.computercraft.peripheral.misc.CreativeChestPeripheral
import site.siredvin.turtlematic.computercraft.peripheral.misc.MimicPeripheral
import site.siredvin.turtlematic.computercraft.peripheral.misc.TurtleChatterPeripheral
import java.util.function.Consumer
import java.util.function.Supplier

object TurtlematicCoreClient {

    val EXTRA_MODELS: Array<String> = arrayOf(
        "turtle/chatter_left",
        "turtle/chatter_right",
        "turtle/mimic_left",
        "turtle/mimic_right",
        "turtle/chunk_vial_left",
        "turtle/chunk_vial_right",
        "turtle/creative_chest_left",
        "turtle/creative_chest_right",
    )

    @Suppress("UNCHECKED_CAST")
    val EXTRA_ENTITY_RENDERERS: Array<Supplier<EntityType<Entity>>> = arrayOf(
        EntityTypes.SHOOTED_ITEM_TYPE as Supplier<EntityType<Entity>>,
    )

    @Suppress("UNCHECKED_CAST")
    fun getEntityRendererProvider(type: EntityType<Entity>): EntityRendererProvider<Entity> {
        if (type == EntityTypes.SHOOTED_ITEM_TYPE.get()) {
            return EntityRendererProvider<ShootedItemProjectile> { ThrownItemRenderer(it) } as EntityRendererProvider<Entity>
        }
        throw IllegalArgumentException("There is no extra renderer for $type")
    }

    fun registerExtraModels(register: Consumer<ResourceLocation>) {
        EXTRA_MODELS.forEach { register.accept(ResourceLocation(TurtlematicCore.MOD_ID, it)) }
    }

    fun <T : ITurtleUpgrade> asClockwise(serializer: Supplier<TurtleUpgradeSerialiser<T>>) {
        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            serializer.get(),
            ClockwiseTurtleModeller(),
        )
    }

    fun onInit() {
        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.TURTLE_CHATTER.get(),
            TurtleUpgradeModeller.sided(
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${TurtleChatterPeripheral.UPGRADE_ID.path}_left"),
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${TurtleChatterPeripheral.UPGRADE_ID.path}_right"),
            ),
        )

        TurtleRenderTrickRegistry.registerTrick(
            TurtleUpgradeSerializers.TURTLE_CHATTER.get(),
            ChattingTurtleRenderTrick,
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.MIMIC.get(),
            TurtleUpgradeModeller.sided(
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${MimicPeripheral.UPGRADE_ID.path}_left"),
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${MimicPeripheral.UPGRADE_ID.path}_right"),
            ),
        )

        TurtleRenderTrickRegistry.registerTrick(
            TurtleUpgradeSerializers.MIMIC.get(),
            MimicTurtleRenderTrick,
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.CREATIVE_CHEST.get(),
            TurtleUpgradeModeller.sided(
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${CreativeChestPeripheral.UPGRADE_ID.path}_left"),
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${CreativeChestPeripheral.UPGRADE_ID.path}_right"),
            ),
        )
        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.CHUNK_VIAL.get(),
            TurtleUpgradeModeller.sided(
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${ChunkVialPeripheral.UPGRADE_ID.path}_left"),
                ResourceLocation(TurtlematicCore.MOD_ID, "turtle/${ChunkVialPeripheral.UPGRADE_ID.path}_right"),
            ),
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.SOUL_SCRAPPER.get(),
            TurtleUpgradeModeller.flatItem(),
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.LAVA_BUCKET.get(),
            TurtleUpgradeModeller.flatItem(),
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.BOW.get(),
            AngleItemTurtleModeller(),
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.PISTON.get(),
            FacingBlockTurtleModeller(),
        )

        ComputerCraftAPIClient.registerTurtleUpgradeModeller(
            TurtleUpgradeSerializers.STICKY_PISTON.get(),
            FacingBlockTurtleModeller(),
        )

        asClockwise(TurtleUpgradeSerializers.AUTOMATA_CORE)
        asClockwise(TurtleUpgradeSerializers.HUSBANDRY_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.NETHERITE_HUSBANDRY_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_HUSBANDRY_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_HUSBANDRY_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.END_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.NETHERITE_END_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_END_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_END_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.PROTECTIVE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.NETHERITE_PROTECTIVE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_PROTECTIVE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_PROTECTIVE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.ENORMOUS_AUTOMATA)

        asClockwise(TurtleUpgradeSerializers.BREWING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.ENCHANTING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.MASON_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.MERCANTILE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.SMITHING_AUTOMATA)

        asClockwise(TurtleUpgradeSerializers.STARBOUND_BREWING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_ENCHANTING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_MASON_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_MERCANTILE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.STARBOUND_SMITHING_AUTOMATA)

        asClockwise(TurtleUpgradeSerializers.CREATIVE_BREWING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_ENCHANTING_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_MASON_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_MERCANTILE_AUTOMATA)
        asClockwise(TurtleUpgradeSerializers.CREATIVE_SMITHING_AUTOMATA)
    }
}
