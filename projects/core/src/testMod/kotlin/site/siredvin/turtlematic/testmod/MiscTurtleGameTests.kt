package site.siredvin.turtlematic.testmod

import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestAssertException
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.gametest.framework.GameTestSequence
import net.minecraft.world.entity.EntityType
import site.siredvin.testiarium.api.TestGroup
import site.siredvin.testiarium.api.thenExecuteFailFast
import site.siredvin.testiarium.cct.CctComputerState
import site.siredvin.testiarium.cct.CctComputers
import site.siredvin.testiarium.cct.CctLuaTests
import site.siredvin.turtlematic.util.ChunkManager
import java.util.UUID

const val LUA_TIMEOUT = 400

fun GameTestHelper.getTurtle(label: String): TurtleBlockEntity {
    val origin = absolutePos(BlockPos.ZERO)
    return BlockPos.betweenClosed(origin.offset(-1, -1, -1), origin.offset(6, 4, 6))
        .mapNotNull { level.getBlockEntity(it) as? TurtleBlockEntity }
        .first { it.saveWithoutMetadata().getString("Label") == label }
}

private fun GameTestHelper.hasForcedChunkRecord(uuid: String): Boolean = ChunkManager.get(level.server.overworld()).hasForceChunk(UUID.fromString(uuid))

fun GameTestHelper.thenTurtleLua(label: String): GameTestSequence {
    CctLuaTests.require(label)
    return startSequence()
        .thenExecuteAfter(1) { CctComputers.enqueue(level.server, label) {} }
        .thenWaitUntil {
            if (CctComputerState.get(label)?.isDone(CctComputerState.DONE) != true) {
                throw GameTestAssertException("Computer '$label' has not finished")
            }
        }
        .thenExecuteFailFast { CctComputerState.get(label)?.check(CctComputerState.DONE) }
}

@TestGroup("turtlematic")
class MiscTurtleGameTests {
    @GameTest(template = "miscturtlegametests.chatter", batch = "misc-chatter", timeoutTicks = LUA_TIMEOUT)
    fun chatter(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.chatter").thenSucceed()

    @GameTest(template = "miscturtlegametests.mimic", batch = "misc-mimic", timeoutTicks = LUA_TIMEOUT)
    fun mimic(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.mimic").thenSucceed()

    @GameTest(template = "miscturtlegametests.creative_chest", batch = "misc-creative-chest", timeoutTicks = LUA_TIMEOUT)
    fun creativeChest(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.creative_chest").thenSucceed()

    @GameTest(template = "miscturtlegametests.chunk_vial_attached", batch = "misc-chunk-vial-attached", timeoutTicks = LUA_TIMEOUT)
    fun chunkVialAttached(helper: GameTestHelper) {
        val uuid = helper.getTurtle("miscturtlegametests.chunk_vial_attached").saveWithoutMetadata().getCompound("LeftUpgradeNbt").getUUID("uuid").toString()
        helper.thenTurtleLua("miscturtlegametests.chunk_vial_attached").thenExecuteFailFast {
            check(helper.hasForcedChunkRecord(uuid)) { "Expected chunk vial force record for $uuid" }
        }.thenSucceed()
    }

    @GameTest(template = "miscturtlegametests.chunk_vial_detached", batch = "misc-chunk-vial-detached", timeoutTicks = 1200)
    @TestGroup("manual")
    fun chunkVialDetached(helper: GameTestHelper) {
        val uuid = helper.getTurtle("miscturtlegametests.chunk_vial_detached").saveWithoutMetadata().getCompound("LeftUpgradeNbt").getUUID("uuid").toString()
        helper.thenTurtleLua("miscturtlegametests.chunk_vial_detached")
            .thenWaitUntil {
                if (helper.hasForcedChunkRecord(uuid)) {
                    throw GameTestAssertException("Detached chunk vial has not released force record $uuid")
                }
            }.thenSucceed()
    }

    @GameTest(template = "miscturtlegametests.inspection_monocle", batch = "misc-inspection-monocle", timeoutTicks = LUA_TIMEOUT)
    fun inspectionMonocle(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.inspection_monocle").thenSucceed()

    @GameTest(template = "miscturtlegametests.soul_scrapper", batch = "misc-soul-scrapper", timeoutTicks = LUA_TIMEOUT)
    fun soulScrapper(helper: GameTestHelper) {
        val pos = helper.getTurtle("miscturtlegametests.soul_scrapper").blockPos
        EntityType.PIG.create(helper.level)!!.also {
            it.moveTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 1.5)
            it.isNoAi = true
            helper.level.addFreshEntity(it)
        }
        helper.thenTurtleLua("miscturtlegametests.soul_scrapper").thenSucceed()
    }

    @GameTest(template = "miscturtlegametests.lava_bucket", batch = "misc-lava-bucket", timeoutTicks = LUA_TIMEOUT)
    fun lavaBucket(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.lava_bucket").thenSucceed()

    @GameTest(template = "miscturtlegametests.bow", batch = "misc-bow", timeoutTicks = LUA_TIMEOUT)
    fun bow(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.bow").thenExecuteFailFast {
        val arrows = helper.getEntities(EntityType.ARROW, BlockPos.ZERO, 64.0)
        check(arrows.any { it.deltaMovement.z > 0 }) { "Expected a southbound arrow, got ${arrows.map { it.deltaMovement }}" }
    }.thenSucceed()

    @GameTest(template = "miscturtlegametests.bow_into_chest", batch = "misc-bow-into-chest", timeoutTicks = LUA_TIMEOUT)
    fun bowIntoChest(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.bow_into_chest").thenSucceed()

    @GameTest(template = "miscturtlegametests.piston", batch = "misc-piston", timeoutTicks = LUA_TIMEOUT)
    fun piston(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.piston").thenSucceed()

    @GameTest(template = "miscturtlegametests.sticky_piston", batch = "misc-sticky-piston", timeoutTicks = LUA_TIMEOUT)
    fun stickyPiston(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.sticky_piston").thenSucceed()
}
