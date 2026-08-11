package site.siredvin.turtlematic.testmod

import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer
import site.siredvin.testiarium.api.TestGroup

@TestGroup("turtlematic")
class ForgedAutomataGameTests {
    @GameTest(template = "forgedautomatagametests.brewing", batch = "automata-brewing", timeoutTicks = LUA_TIMEOUT)
    fun brewing(helper: GameTestHelper) = helper.thenTurtleLua("forgedautomatagametests.brewing").thenSucceed()

    @GameTest(template = "forgedautomatagametests.smithing", batch = "automata-smithing", timeoutTicks = LUA_TIMEOUT)
    fun smithing(helper: GameTestHelper) = helper.thenTurtleLua("forgedautomatagametests.smithing").thenSucceed()

    @GameTest(template = "forgedautomatagametests.enchanting", batch = "automata-enchanting", timeoutTicks = LUA_TIMEOUT)
    fun enchanting(helper: GameTestHelper) {
        val pos = helper.getTurtle("forgedautomatagametests.enchanting").blockPos
        helper.level.addFreshEntity(ExperienceOrb(helper.level, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 1000))
        helper.thenTurtleLua("forgedautomatagametests.enchanting").thenSucceed()
    }

    @GameTest(template = "forgedautomatagametests.mason", batch = "automata-mason", timeoutTicks = LUA_TIMEOUT)
    fun mason(helper: GameTestHelper) = helper.thenTurtleLua("forgedautomatagametests.mason").thenSucceed()

    @GameTest(template = "forgedautomatagametests.mason_shape", batch = "automata-mason-shape", timeoutTicks = LUA_TIMEOUT)
    fun masonShape(helper: GameTestHelper) = helper.thenTurtleLua("forgedautomatagametests.mason_shape").thenSucceed()

    @GameTest(template = "forgedautomatagametests.mercantile", batch = "automata-mercantile", timeoutTicks = LUA_TIMEOUT)
    fun mercantile(helper: GameTestHelper) {
        val turtle = helper.getTurtle("forgedautomatagametests.mercantile")
        val pos = turtle.blockPos.relative(turtle.direction)
        val villager = EntityType.VILLAGER.create(helper.level) as Villager
        villager.moveTo(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
        villager.isNoAi = true
        villager.offers.add(MerchantOffer(ItemCost(Items.EMERALD, 2), ItemStack(Items.BREAD, 3), 10, 1, 0f).apply { setToOutOfStock() })
        helper.level.addFreshEntity(villager)
        helper.thenTurtleLua("forgedautomatagametests.mercantile").thenSucceed()
    }
}
