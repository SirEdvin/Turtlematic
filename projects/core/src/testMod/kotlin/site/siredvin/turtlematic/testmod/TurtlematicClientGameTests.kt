package site.siredvin.turtlematic.testmod

import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTestHelper
import site.siredvin.testiarium.api.ClientGameTest
import site.siredvin.testiarium.api.TestGroup
import site.siredvin.testiarium.fixture.client.positionAt
import site.siredvin.testiarium.fixture.client.thenScreenshot

@TestGroup("turtlematic-client")
class TurtlematicClientGameTests {
    @ClientGameTest(template = "miscturtlegametests.mimic", timeoutTicks = LUA_TIMEOUT)
    fun rendersMimic(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.mimic")
        .thenExecute { helper.positionAt(BlockPos(2, 2, 6), 180f, 15f) }
        .thenScreenshot("mimic-turtle")
        .thenSucceed()

    @ClientGameTest(template = "miscturtlegametests.chatter", timeoutTicks = LUA_TIMEOUT)
    fun rendersChatter(helper: GameTestHelper) = helper.thenTurtleLua("miscturtlegametests.chatter")
        .thenExecute { helper.positionAt(BlockPos(2, 2, 6), 180f, 15f) }
        .thenScreenshot("chatter-turtle")
        .thenSucceed()
}
