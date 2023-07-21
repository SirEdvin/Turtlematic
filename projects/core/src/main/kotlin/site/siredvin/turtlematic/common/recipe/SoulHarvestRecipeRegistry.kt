package site.siredvin.turtlematic.common.recipe

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.item.Item
import site.siredvin.turtlematic.common.setup.Items

object SoulHarvestRecipeRegistry {
    val CONSUMED_ENTITY_COUNT = "consumed_entity_count"
    val CONSUMED_ENTITY_NAME = "consumed_entity_name"
    val CONSUMER_ENTITY_COMPOUND = "consumed_entity_compound"

    private val RECIPE_REGISTRY: MutableMap<Item, MutableList<SoulHarvestRecipe>> = hashMapOf()
    private val REVERSE_RECIPE_REGISTRY: MutableMap<Item, Pair<SoulHarvestRecipe, Item>> = hashMapOf()

    fun addRecipe(targetItem: Item, recipe: SoulHarvestRecipe) {
        if (!RECIPE_REGISTRY.containsKey(targetItem)) {
            RECIPE_REGISTRY[targetItem] = mutableListOf()
        }
        RECIPE_REGISTRY[targetItem]!!.add(recipe)
        REVERSE_RECIPE_REGISTRY[recipe.resultSoul] = Pair(recipe, targetItem)
    }

    fun get(item: Item): Pair<SoulHarvestRecipe, Item>? {
        return REVERSE_RECIPE_REGISTRY[item]
    }

    fun searchRecipe(targetItem: Item, entity: Entity): SoulHarvestRecipe? {
        if (!RECIPE_REGISTRY.containsKey(targetItem)) {
            return null
        }
        val recipe = RECIPE_REGISTRY[targetItem]!!.stream().filter { it.isSuitable(entity) }.findAny()
        if (recipe.isEmpty) {
            return null
        }
        return recipe.get()
    }

    fun searchRecipe(targetItem: Item, name: String): SoulHarvestRecipe? {
        if (!RECIPE_REGISTRY.containsKey(targetItem)) {
            return null
        }
        return RECIPE_REGISTRY[targetItem]!!.find { it.isSuitable(name) }
    }

    fun injectAutomataCoreRecipes() {
        val endSoulRecord =
            SoulHarvestRecipe(
                listOf(SimpleSoulHarvestIngredient(EntityType.ENDERMAN, 3)),
                Items.END_AUTOMATA_CORE.get(),
            )
        val husbandrySoulRecord =
            SoulHarvestRecipe(
                listOf(
                    SimpleSoulHarvestIngredient(EntityType.PIG, 1),
                    SimpleSoulHarvestIngredient(EntityType.SHEEP, 1),
                    SimpleSoulHarvestIngredient(EntityType.COW, 1),
                    SimpleSoulHarvestIngredient(EntityType.CHICKEN, 1),
                ),
                Items.HUSBANDRY_AUTOMATA_CORE.get(),
            )
        val protectiveSoulRecord = SoulHarvestRecipe(
            listOf(
                SimpleSoulHarvestIngredient(EntityType.IRON_GOLEM, 1),
            ),
            Items.PROTECTIVE_AUTOMATA_CORE.get(),
        )
        addRecipe(Items.AUTOMATA_CORE.get(), endSoulRecord)
        addRecipe(Items.AUTOMATA_CORE.get(), husbandrySoulRecord)
        addRecipe(Items.AUTOMATA_CORE.get(), protectiveSoulRecord)
    }

    fun injectForgedAutomataCoreRecipes() {
        val brewingAutomataRecord = SoulHarvestRecipe(
            listOf(VillagerSoulHarvestIngredient(VillagerProfession.CLERIC)),
            Items.BREWING_AUTOMATA_CORE.get(),
        )
        val masonAutomataRecipe = SoulHarvestRecipe(
            listOf(VillagerSoulHarvestIngredient(VillagerProfession.MASON)),
            Items.MASON_AUTOMATA_CORE.get(),
        )
        val enchantingAutomataRecord = SoulHarvestRecipe(
            listOf(VillagerSoulHarvestIngredient(VillagerProfession.LIBRARIAN)),
            Items.ENCHANTING_AUTOMATA_CORE.get(),
        )
        val smithingAutomataRecord = SoulHarvestRecipe(
            listOf(VillagerSoulHarvestIngredient(VillagerProfession.TOOLSMITH)),
            Items.SMITHING_AUTOMATA_CORE.get(),
        )
        val mercantileSoulRecord =
            SoulHarvestRecipe(
                listOf(
                    SimpleSoulHarvestIngredient(EntityType.WANDERING_TRADER, 1),
                ),
                Items.MERCANTILE_AUTOMATA_CORE.get(),
            )

        addRecipe(Items.FORGED_AUTOMATA_CORE.get(), brewingAutomataRecord)
        addRecipe(Items.FORGED_AUTOMATA_CORE.get(), enchantingAutomataRecord)
        addRecipe(Items.FORGED_AUTOMATA_CORE.get(), smithingAutomataRecord)
        addRecipe(Items.FORGED_AUTOMATA_CORE.get(), mercantileSoulRecord)
        addRecipe(Items.FORGED_AUTOMATA_CORE.get(), masonAutomataRecipe)
    }
}
