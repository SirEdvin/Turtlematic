package site.siredvin.turtlematic.testmod

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.Blocks
import site.siredvin.testiarium.api.TestGroup
import site.siredvin.testiarium.api.immediate
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.AutomataCoreTier
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.setup.Items
import site.siredvin.turtlematic.util.ChunkManager
import site.siredvin.turtlematic.util.toNBT
import java.util.UUID

@TestGroup("turtlematic")
class TurtlematicGameTests {
    @GameTest(template = "empty")
    fun stableItemsAreRegistered(helper: GameTestHelper) = helper.immediate {
        mapOf(
            "inspection_monocle" to Items.INSPECTION_MONOCLE.get(),
            "automata_core" to Items.AUTOMATA_CORE.get(),
            "forged_automata_core" to Items.FORGED_AUTOMATA_CORE.get(),
            "soul_vial" to Items.SOUL_VIAL.get(),
            "turtle_chatter" to Items.TURTLE_CHATTER.get(),
        ).forEach { (path, expected) ->
            val id = TurtlematicCore.id(path)
            check(BuiltInRegistries.ITEM.get(id) === expected) { "Expected registered item $id" }
            check(BuiltInRegistries.ITEM.getKey(expected) == id) { "Expected stable registry ID $id" }
        }
    }

    @GameTest(template = "empty")
    fun readsSoulProgressNbt(helper: GameTestHelper) = helper.immediate {
        val consumedData = CompoundTag().apply {
            put("minecraft:enderman", CompoundTag())
        }
        val stack = Items.AUTOMATA_CORE.get().defaultInstance
        stack.orCreateTag.put(SoulHarvestRecipeRegistry.CONSUMER_ENTITY_COMPOUND, consumedData)

        val feedable = stack.item as ISoulFeedableItem
        check(feedable.getActiveRecipe(stack) != null) { "Expected NBT soul progress to remain readable" }
    }

    @GameTest(template = "empty")
    fun soulVialFillsFromSoulSand(helper: GameTestHelper) = helper.immediate {
        val target = BlockPos(1, 1, 1)
        val player = helper.makeMockPlayer()
        val playerPos = helper.absolutePos(BlockPos(1, 1, 3))
        val vial = Items.SOUL_VIAL.get().defaultInstance

        player.moveTo(playerPos.x + 0.5, playerPos.y.toDouble(), playerPos.z + 0.5, 180f, 30f)
        player.setItemInHand(InteractionHand.MAIN_HAND, vial)

        repeat(3) { index ->
            helper.setBlock(target, Blocks.SOUL_SAND)
            val result = Items.SOUL_VIAL.get().use(helper.level, player, InteractionHand.MAIN_HAND)

            check(result.result.consumesAction()) { "Expected soul vial use to succeed" }
            check(result.`object` === vial) { "Expected partially filled vial to remain the same stack" }
            check(vial.tag?.getInt("CustomModelData") == index + 1) { "Expected vial fill level ${index + 1}" }
            helper.assertBlockPresent(Blocks.SAND, target)
        }

        helper.setBlock(target, Blocks.SOUL_SAND)
        val completed = Items.SOUL_VIAL.get().use(helper.level, player, InteractionHand.MAIN_HAND)
        check(completed.result.consumesAction()) { "Expected final soul collection to consume the use" }
        check(completed.`object`.`is`(Items.FILLED_SOUL_VIAL.get())) { "Expected filled soul vial after four uses" }
        helper.assertBlockPresent(Blocks.SAND, target)
    }

    @GameTest(template = "empty")
    fun soulHarvestCompletesAndRejectsConflictingRecipe(helper: GameTestHelper) = helper.immediate {
        val player = helper.makeMockPlayer()
        val core = Items.AUTOMATA_CORE.get().defaultInstance
        val first = helper.spawn(EntityType.ENDERMAN, BlockPos(1, 1, 1))
        val firstResult = Items.AUTOMATA_CORE.get().consumeEntitySoul(core, player, first)

        check(firstResult.first === core && firstResult.second == null) { "Expected first enderman soul to advance the end recipe" }
        check(!first.isAlive) { "Expected consumed enderman to be removed" }

        val conflictingPig = helper.spawn(EntityType.PIG, BlockPos(1, 1, 1))
        val conflictResult = Items.AUTOMATA_CORE.get().consumeEntitySoul(core, player, conflictingPig)
        check(conflictResult.first == null && conflictResult.second != null) { "Expected active end recipe to reject a pig soul" }
        check(conflictingPig.isAlive) { "Rejected entity must not be consumed" }

        repeat(2) { index ->
            val enderman = helper.spawn(EntityType.ENDERMAN, BlockPos(1, 1, 1))
            val result = Items.AUTOMATA_CORE.get().consumeEntitySoul(core, player, enderman)
            check(result.second == null && !enderman.isAlive) { "Expected enderman ${index + 2} to be consumed" }
            if (index == 0) {
                check(result.first === core) { "Expected recipe to remain incomplete after two souls" }
            } else {
                check(result.first?.`is`(Items.END_AUTOMATA_CORE.get()) == true) { "Expected end automata core after three souls" }
            }
        }
    }

    @GameTest(template = "empty")
    fun chunkManagerRoundTripsSavedRecords(helper: GameTestHelper) = helper.immediate {
        val owner = UUID.randomUUID()
        val expectedPos = ChunkPos(12, -7)
        val input = CompoundTag().apply {
            put(
                "forcedChunks",
                CompoundTag().apply {
                    put(
                        owner.toString(),
                        CompoundTag().apply {
                            putString("dimensionName", helper.level.dimension().location().toString())
                            put("pos", toNBT(expectedPos))
                        },
                    )
                },
            )
        }

        val loaded = ChunkManager.load(input)
        check(loaded.hasForceChunk(owner)) { "Expected loaded owner record" }
        check(loaded.save(CompoundTag()) == input) { "Expected chunk record to survive save/load" }
    }

    @GameTest(template = "empty")
    fun tierDurabilityPolicies(helper: GameTestHelper) = helper.immediate {
        check(!AutomataCoreTier.TIER1.needRestoreDurability()) { "Tier 1 must not refund durability" }
        check(AutomataCoreTier.TIER4.needRestoreDurability()) { "Tier 4 must refund durability" }
        check(AutomataCoreTier.CREATIVE.needRestoreDurability()) { "Creative tier must refund durability" }
        // ponytail: do not make the tier 3 random policy deterministic by mutating shared config.
    }
}
