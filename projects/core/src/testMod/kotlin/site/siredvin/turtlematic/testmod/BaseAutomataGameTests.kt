package site.siredvin.turtlematic.testmod

import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
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
        val turtle = helper.getTurtle("baseautomatagametests.automata")
        val pos = turtle.blockPos
        helper.level.setBlockAndUpdate(pos.relative(turtle.direction), Blocks.CHEST.defaultBlockState())
        helper.level.addFreshEntity(ItemEntity(helper.level, pos.x + 1.5, pos.y.toDouble(), pos.z + 0.5, ItemStack(Items.APPLE, 3)))
        helper.thenTurtleLua("baseautomatagametests.automata").thenSucceed()
    }

    @GameTest(template = "baseautomatagametests.husbandry", batch = "automata-husbandry", timeoutTicks = LUA_TIMEOUT)
    fun husbandry(helper: GameTestHelper) {
        val turtle = helper.getTurtle("baseautomatagametests.husbandry")
        val upgradeData = CompoundTag().apply { putInt("husbandryPoints", 200) }
        turtle.access.setUpgradeData(
            TurtleSide.LEFT,
            DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(upgradeData)).build(),
        )
        helper.level.setBlockAndUpdate(turtle.blockPos.relative(turtle.direction), Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE))
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
