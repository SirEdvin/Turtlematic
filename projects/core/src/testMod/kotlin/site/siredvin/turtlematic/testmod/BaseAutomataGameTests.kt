package site.siredvin.turtlematic.testmod

import net.minecraft.core.BlockPos
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import site.siredvin.testiarium.api.TestGroup

@TestGroup("turtlematic")
class BaseAutomataGameTests {
    private fun GameTestHelper.spawn(label: String, type: EntityType<*>, offset: BlockPos) {
        type.create(level)!!.also {
            val pos = getTurtle(label).blockPos.offset(offset)
            it.moveTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
            it.isNoGravity = true
            (it as? Mob)?.isNoAi = true
            level.addFreshEntity(it)
        }
    }

    @GameTest(template = "baseautomatagametests.automata", batch = "automata", timeoutTicks = LUA_TIMEOUT)
    fun automata(helper: GameTestHelper) {
        val pos = helper.getTurtle("baseautomatagametests.automata").blockPos
        helper.level.addFreshEntity(ItemEntity(helper.level, pos.x + 1.5, pos.y.toDouble(), pos.z + 0.5, ItemStack(Items.APPLE, 3)))
        helper.thenTurtleLua("baseautomatagametests.automata").thenSucceed()
    }

    @GameTest(template = "baseautomatagametests.husbandry", batch = "automata-husbandry", timeoutTicks = LUA_TIMEOUT)
    fun husbandry(helper: GameTestHelper) {
        helper.spawn("baseautomatagametests.husbandry", EntityType.COW, BlockPos(0, 1, 0))
        helper.thenTurtleLua("baseautomatagametests.husbandry").thenSucceed()
    }

    @GameTest(template = "baseautomatagametests.end", batch = "automata-end", timeoutTicks = LUA_TIMEOUT)
    fun end(helper: GameTestHelper) = helper.thenTurtleLua("baseautomatagametests.end").thenSucceed()

    @GameTest(template = "baseautomatagametests.protective", batch = "automata-protective", timeoutTicks = LUA_TIMEOUT)
    fun protective(helper: GameTestHelper) {
        helper.spawn("baseautomatagametests.protective", EntityType.ZOMBIE, BlockPos(0, 1, 0))
        helper.spawn("baseautomatagametests.protective", EntityType.COW, BlockPos(1, 0, 0))
        helper.thenTurtleLua("baseautomatagametests.protective").thenSucceed()
    }

    @GameTest(template = "baseautomatagametests.enormous", batch = "automata-enormous", timeoutTicks = LUA_TIMEOUT)
    fun enormous(helper: GameTestHelper) {
        val pos = helper.getTurtle("baseautomatagametests.enormous").blockPos
        helper.spawn("baseautomatagametests.enormous", EntityType.ARMOR_STAND, BlockPos(1, 0, 0))
        helper.level.addFreshEntity(ItemEntity(helper.level, pos.x + 1.5, pos.y.toDouble(), pos.z + 0.5, ItemStack(Items.DIAMOND)))
        helper.thenTurtleLua("baseautomatagametests.enormous").thenSucceed()
    }
}
