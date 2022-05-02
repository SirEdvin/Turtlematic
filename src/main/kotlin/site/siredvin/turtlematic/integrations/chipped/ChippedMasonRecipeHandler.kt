package site.siredvin.turtlematic.integrations.chipped

import com.grimbo.chipped.recipe.ChippedRecipe
import com.grimbo.chipped.registry.ChippedSerializer
import net.minecraft.world.Container
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.computercraft.peripheral.forged.MasonAutomataCorePeripheral

class ChippedMasonRecipeHandler: MasonAutomataCorePeripheral.MasonRecipeHandler {

    override fun getAlternatives(level: Level, fakeContainer: Container): List<ItemStack> {
        val alternatives = mutableListOf<ItemStack>()
        level.recipeManager.getRecipesFor(ChippedSerializer.BOTANIST_WORKBENCH_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        level.recipeManager.getRecipesFor(ChippedSerializer.MASON_TABLE_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        level.recipeManager.getRecipesFor(ChippedSerializer.CARPENTERS_TABLE_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        level.recipeManager.getRecipesFor(ChippedSerializer.GLASSBLOWER_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        level.recipeManager.getRecipesFor(ChippedSerializer.LOOM_TABLE_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        level.recipeManager.getRecipesFor(ChippedSerializer.MECHANIST_WORKBENCH_TYPE, fakeContainer, level).forEach { alternatives.addAll(it.getResults(fakeContainer).toList()) }
        return alternatives
    }

    override fun getRecipe(level: Level, fakeContainer: Container, targetItem: Item): Recipe<Container>? {
        return level.recipeManager.getRecipesFor(ChippedSerializer.BOTANIST_WORKBENCH_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} } ?:
        level.recipeManager.getRecipesFor(ChippedSerializer.MASON_TABLE_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} } ?:
        level.recipeManager.getRecipesFor(ChippedSerializer.CARPENTERS_TABLE_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} } ?:
        level.recipeManager.getRecipesFor(ChippedSerializer.GLASSBLOWER_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} } ?:
        level.recipeManager.getRecipesFor(ChippedSerializer.LOOM_TABLE_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} } ?:
        level.recipeManager.getRecipesFor(ChippedSerializer.MECHANIST_WORKBENCH_TYPE, fakeContainer, level).find { it.getResults(fakeContainer).anyMatch { it1 -> it1.`is`(targetItem)} }
    }

    override fun produce(
        level: Level,
        fakeContainer: Container,
        targetItem: Item,
        recipe: Recipe<Container>,
        limit: Int
    ): ItemStack {
        if (recipe !is ChippedRecipe)
            return ItemStack.EMPTY
        val resultOpt = recipe.getResults(fakeContainer).filter { it.`is`(targetItem) }.findFirst()
        if (resultOpt.isEmpty)
            return ItemStack.EMPTY
        var consumedAmount = 0
        val output = resultOpt.get().copy()
        output.count = 0
        while (recipe.matches(fakeContainer, level) && limit > consumedAmount) {
            fakeContainer.getItem(0).shrink(1)
            consumedAmount++
            output.grow(resultOpt.get().count)
        }
        return output
    }

    override val handlerID: String
        get() = "chipped"
    override val workWith: List<Class<*>>
        get() = listOf(ChippedRecipe::class.java)
}