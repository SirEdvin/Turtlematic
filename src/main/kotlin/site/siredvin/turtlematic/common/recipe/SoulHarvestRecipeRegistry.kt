package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.common.setup.Items
import java.util.function.Consumer

object SoulHarvestRecipeRegistry {
    val CONSUMED_ENTITY_COUNT = "consumed_entity_count"
    val CONSUMED_ENTITY_NAME = "consumed_entity_name"
    val CONSUMER_ENTITY_COMPOUND = "consumed_entity_compound"

    private val RECIPE_REGISTRY: MutableMap<Item, MutableList<SoulHarvestRecipe>> = hashMapOf()
    private val REVERSE_RECIPE_REGISTRY: MutableMap<Item, SoulHarvestRecipe> = hashMapOf()

    fun addRecipe(targetItem: Item, recipe: SoulHarvestRecipe) {
        if (!RECIPE_REGISTRY.containsKey(targetItem))
            RECIPE_REGISTRY[targetItem] = mutableListOf()
        RECIPE_REGISTRY[targetItem]!!.add(recipe)
        REVERSE_RECIPE_REGISTRY[recipe.resultSoul] = recipe
    }

    fun get(item: Item): SoulHarvestRecipe? {
        return REVERSE_RECIPE_REGISTRY[item]
    }

    fun searchRecipe(targetItem: Item, entity: Entity): SoulHarvestRecipe? {
        if (!RECIPE_REGISTRY.containsKey(targetItem))
            return null
        val recipe = RECIPE_REGISTRY[targetItem]!!.stream().filter { it.isSuitable(entity) }.findAny()
        if (recipe.isEmpty)
            return null
        return recipe.get()
    }

    fun searchRecipe(targetItem: Item, name: String): SoulHarvestRecipe? {
        if (!RECIPE_REGISTRY.containsKey(targetItem))
            return null
        val recipe = RECIPE_REGISTRY[targetItem]!!.stream().filter { it.isSuitable(name) }.findAny()
        if (recipe.isEmpty)
            return null
        return recipe.get()
    }

    fun injectAutomataCoreRecipes() {
        val endSoulRecord =
            SoulHarvestRecipe(
                listOf(SimpleSoulHarvestIngredient(EntityType.ENDERMAN, 3)), Items.END_AUTOMATA_CORE
            )
        val husbandrySoulRecord =
            SoulHarvestRecipe(
                listOf(
                    SimpleSoulHarvestIngredient(EntityType.PIG, 1),
                    SimpleSoulHarvestIngredient(EntityType.SHEEP, 1),
                    SimpleSoulHarvestIngredient(EntityType.COW, 1),
                    SimpleSoulHarvestIngredient(EntityType.CHICKEN, 1),
                ), Items.HUSBANDRY_AUTOMATA_CORE
            )
        addRecipe(Items.AUTOMATA_CORE, endSoulRecord)
        addRecipe(Items.AUTOMATA_CORE, husbandrySoulRecord)
    }
}