package site.siredvin.turtlematic.testmod

import dan200.computercraft.api.turtle.TurtleSide
import dan200.computercraft.impl.TurtleUpgrades
import dan200.computercraft.shared.ModRegistry
import dan200.computercraft.shared.turtle.blocks.TurtleBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import site.siredvin.testiarium.api.TestGroup
import site.siredvin.testiarium.api.immediate
import site.siredvin.turtlematic.common.setup.Items as TurtlematicItems

@TestGroup("turtlematic")
class BaseAutomataGameTests {
    private fun <T : Entity> GameTestHelper.spawn(label: String, type: EntityType<T>, offset: BlockPos): T = type.create(level)!!.also {
        val pos = getTurtle(label).blockPos.offset(offset)
        it.moveTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
        it.isNoGravity = true
        (it as? Mob)?.isNoAi = true
        level.addFreshEntity(it)
    }

    @GameTest(template = "baseautomatagametests.automata", batch = "automata", timeoutTicks = LUA_TIMEOUT)
    fun automata(helper: GameTestHelper) {
        val turtle = helper.getTurtle("baseautomatagametests.automata")
        val pos = turtle.blockPos
        helper.level.setBlockAndUpdate(pos.relative(turtle.direction), Blocks.CHEST.defaultBlockState())
        helper.level.addFreshEntity(ItemEntity(helper.level, pos.x + 1.5, pos.y.toDouble(), pos.z + 0.5, ItemStack(Items.APPLE, 3)))
        helper.thenTurtleLua("baseautomatagametests.automata").thenSucceed()
    }

    // CC 1.121.0 deprecates setUpgradeData, but it still recomputes the stored upgrade item with the data.
    @Suppress("DEPRECATION")
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

    @GameTest(template = "empty")
    fun creativeCoreDisablesFuel(helper: GameTestHelper) = helper.immediate {
        val pos = BlockPos(1, 1, 1)
        helper.setBlock(pos, ModRegistry.Blocks.TURTLE_NORMAL.get())
        val access = (helper.getBlockEntity(pos) as TurtleBlockEntity).access
        val core = TurtleUpgrades.instance().get(helper.level.registryAccess(), TurtlematicItems.END_AUTOMATA_CORE.get().defaultInstance)
        access.setUpgrade(TurtleSide.LEFT, checkNotNull(core) { "Expected an end core turtle upgrade" })
        check(access.isFuelNeeded) { "Expected a turtle with a regular core to need fuel" }
        val creativeCore = TurtleUpgrades.instance().get(helper.level.registryAccess(), TurtlematicItems.CREATIVE_END_AUTOMATA_CORE.get().defaultInstance)
        access.setUpgrade(TurtleSide.LEFT, checkNotNull(creativeCore) { "Expected a creative core turtle upgrade" })
        check(!access.isFuelNeeded) { "Expected a creative core to disable fuel" }
    }

    @GameTest(template = "baseautomatagametests.protective", batch = "automata-protective", timeoutTicks = LUA_TIMEOUT)
    fun protective(helper: GameTestHelper) {
        // GameTest worlds sit at noon under skylight-passing barriers, so the zombie burns for as many ticks as the Lua takes; a non-damageable helmet prevents ignition.
        helper.spawn("baseautomatagametests.protective", EntityType.ZOMBIE, BlockPos(0, 1, 0))
            .setItemSlot(EquipmentSlot.HEAD, ItemStack(Items.CARVED_PUMPKIN))
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
