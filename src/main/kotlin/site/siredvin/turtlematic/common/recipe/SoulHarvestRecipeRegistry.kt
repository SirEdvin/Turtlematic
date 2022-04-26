package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.common.setup.Items
import java.util.function.Consumer

object SoulHarvestRecipeRegistry {
    val CONSUMED_ENTITY_COUNT = "consumed_entity_count"
    val CONSUMED_ENTITY_NAME = "consumed_entity_name"
    val CONSUMER_ENTITY_COMPOUND = "consumed_entity_compound"

    private val RECIPE_REGISTRY: MutableMap<Item, MutableMap<String, SoulHarvestRecipe>> = hashMapOf()
    private val REVERSE_RECIPE_REGISTRY: MutableMap<Item, SoulHarvestRecipe> = hashMapOf()

    fun addRecipe(targetItem: Item, recipe: SoulHarvestRecipe) {
        if (!RECIPE_REGISTRY.containsKey(targetItem))
            RECIPE_REGISTRY[targetItem] = hashMapOf()
        recipe.ingredients.keys.forEach(Consumer { entityType: String ->
            if (RECIPE_REGISTRY[targetItem]!!.containsKey(entityType) && RECIPE_REGISTRY[targetItem]!![entityType] != recipe)
                throw IllegalArgumentException("Entity type collision for $targetItem")
            RECIPE_REGISTRY[targetItem]!![entityType] = recipe
        })
        REVERSE_RECIPE_REGISTRY[recipe.resultSoul] = recipe
    }

    fun get(item: Item): SoulHarvestRecipe? {
        return REVERSE_RECIPE_REGISTRY[item]
    }

    fun searchRecipe(targetItem: Item, entityType: String): SoulHarvestRecipe? {
        if (!RECIPE_REGISTRY.containsKey(targetItem))
            return null
        return RECIPE_REGISTRY[targetItem]!![entityType]
    }

    fun injectAutomataCoreRecipes() {
        val endSoulRecord =
            SoulHarvestRecipe(
                object : HashMap<String, Int>() {
                    init {
                        put(EntityType.getKey(EntityType.ENDERMAN).toString(), 3)
                    }
                }, Items.END_AUTOMATA_CORE
            )
        val husbandrySoulRecord =
            SoulHarvestRecipe(
                object : HashMap<String, Int>() {
                    init {
                        put(EntityType.getKey(EntityType.SHEEP).toString(), 1)
                        put(EntityType.getKey(EntityType.COW).toString(), 1)
                        put(EntityType.getKey(EntityType.CHICKEN).toString(), 1)
                        put(EntityType.getKey(EntityType.PIG).toString(), 1)
                    }
                }, Items.HUSBANDRY_AUTOMATA_CORE
            )
        addRecipe(Items.AUTOMATA_CORE, endSoulRecord)
        addRecipe(Items.AUTOMATA_CORE, husbandrySoulRecord)
    }
}