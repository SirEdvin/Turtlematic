package site.siredvin.turtlematic.testmod

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.nbt.CompoundTag
import site.siredvin.testiarium.api.TestGroup
import site.siredvin.testiarium.api.immediate
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.turtlematic.api.ISoulFeedableItem
import site.siredvin.turtlematic.common.recipe.SoulHarvestRecipeRegistry
import site.siredvin.turtlematic.common.setup.Items

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
}
