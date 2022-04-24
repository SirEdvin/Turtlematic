package site.siredvin.lib.data

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.core.Registry
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.ItemLike
import java.util.function.Consumer

class TweakedShapedRecipeBuilder(val _result: Item, val count: Int) {
    private val rows: MutableList<String> = Lists.newArrayList()
    private val key: MutableMap<Char, Ingredient> = Maps.newLinkedHashMap()
    private var group: String? = null

    companion object {
        fun shaped(itemLike: ItemLike): TweakedShapedRecipeBuilder {
            return shaped(itemLike, 1)
        }

        fun shaped(itemLike: ItemLike, i: Int): TweakedShapedRecipeBuilder {
            return TweakedShapedRecipeBuilder(itemLike.asItem(), i)
        }
    }

    fun define(character: Char, tagKey: TagKey<Item>): TweakedShapedRecipeBuilder {
        return this.define(character, Ingredient.of(tagKey))
    }

    fun define(character: Char, itemLike: ItemLike): TweakedShapedRecipeBuilder {
        return this.define(character, Ingredient.of(itemLike))
    }

    fun define(character: Char, ingredient: Ingredient): TweakedShapedRecipeBuilder {
        return if (key.containsKey(character)) {
            throw IllegalArgumentException("Symbol '$character' is already defined!")
        } else if (character == ' ') {
            throw IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined")
        } else {
            key[character] = ingredient
            this
        }
    }

    fun pattern(string: String): TweakedShapedRecipeBuilder {
        return if (rows.isNotEmpty() && string.length != rows[0].length) {
            throw IllegalArgumentException("Pattern must be the same width on every line!")
        } else {
            rows.add(string)
            this
        }
    }

    fun group(string: String): TweakedShapedRecipeBuilder {
        group = string
        return this
    }

    val result: Item
        get() = _result

    fun save(consumer: Consumer<FinishedRecipe>) {
        this.save(consumer, Registry.ITEM.getKey(result))
    }

    fun save(consumer: Consumer<FinishedRecipe>, resourceLocation: ResourceLocation) {
        ensureValid(resourceLocation)
        val var10004 = result
        val var10005 = count
        val var10006 = if (group == null) "" else group!!
        val var10007: List<String> = rows
        val var10008: Map<Char, Ingredient> = key
        consumer.accept(
            Result(
                resourceLocation,
                var10004,
                var10005,
                var10006,
                var10007,
                var10008
            )
        )
    }

    private fun ensureValid(resourceLocation: ResourceLocation) {
        check(rows.isNotEmpty()) { "No pattern is defined for shaped recipe $resourceLocation!" }
        val set: MutableSet<Char> = Sets.newHashSet(key.keys)
        set.remove(' ')
        val var3: Iterator<*> = rows.iterator()
        while (var3.hasNext()) {
            val string = var3.next() as String
            for (element in string) {
                check(!(!key.containsKey(element) && element != ' ')) { "Pattern in recipe $resourceLocation uses undefined symbol '$element'" }
                set.remove(element)
            }
        }
        check(set.isEmpty()) { "Ingredients are defined but not used in pattern for recipe $resourceLocation" }
        check(!(rows.size == 1 && rows[0].length == 1)) { "Shaped recipe $resourceLocation only takes in a single item - should it be a shapeless recipe instead?" }
    }

    private class Result(
        private val id: ResourceLocation,
        private val result: Item,
        private val count: Int,
        private val group: String,
        private val pattern: List<String>,
        private val key: Map<Char, Ingredient>
    ) :
        FinishedRecipe {
        override fun serializeRecipeData(jsonObject: JsonObject) {
            if (group.isNotEmpty()) {
                jsonObject.addProperty("group", group)
            }
            val jsonArray = JsonArray()
            val var3: Iterator<*> = pattern.iterator()
            while (var3.hasNext()) {
                val string = var3.next() as String
                jsonArray.add(string)
            }
            jsonObject.add("pattern", jsonArray)
            val jsonObject2 = JsonObject()
            val var7: Iterator<*> = key.entries.iterator()
            while (var7.hasNext()) {
                val (key1, value) = var7.next() as Map.Entry<*, *>
                jsonObject2.add(key1.toString(), (value as Ingredient).toJson())
            }
            jsonObject.add("key", jsonObject2)
            val jsonObject3 = JsonObject()
            jsonObject3.addProperty("item", Registry.ITEM.getKey(result).toString())
            if (count > 1) {
                jsonObject3.addProperty("count", count)
            }
            jsonObject.add("result", jsonObject3)
        }

        override fun getType(): RecipeSerializer<*> {
            return RecipeSerializer.SHAPED_RECIPE
        }

        override fun getId(): ResourceLocation {
            return id
        }

        override fun serializeAdvancement(): JsonObject? {
            return null
        }

        override fun getAdvancementId(): ResourceLocation? {
            return null
        }
    }
}