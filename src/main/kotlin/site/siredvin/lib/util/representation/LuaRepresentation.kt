package site.siredvin.lib.util.representation

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Shearable
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.lib.ext.toRelative
import java.util.stream.Collectors
import java.util.stream.Stream

object LuaRepresentation {

    fun forBlockState(state: BlockState): MutableMap<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = state.block.name.string
        data["tags"] = tagsToList(state.tags)
        return data
    }

    fun forEntity(entity: Entity): MutableMap<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["id"] = entity.id
        data["uuid"] = entity.stringUUID
        data["category"] = entity.type.category.name
        data["type"] = entity.type.description.string
        data["name"] = entity.name.string
        data["tags"] = entity.tags
        return data
    }

    fun <T: Entity> withPos(entity: T, facing: Direction, center: BlockPos, converter: (T) -> (MutableMap<String, Any>)):  MutableMap<String, Any> {
        val base = converter(entity)
        base.putAll(forBlockPos(entity.blockPosition(), facing, center))
        return base
    }

    fun <T> withPos(value: T, pos: BlockPos, facing: Direction, center: BlockPos, converter: (T) -> (MutableMap<String, Any>)):  MutableMap<String, Any> {
        val base = converter(value)
        base.putAll(forBlockPos(pos, facing, center))
        return base
    }

    fun forBlockPos(pos: BlockPos, facing: Direction, center: BlockPos): MutableMap<String, Any> {
        val transformedPos = pos.subtract(center).toRelative(facing)
        val map: MutableMap<String, Any> = HashMap()
        map["x"] = transformedPos.x
        map["y"] = transformedPos.y
        map["z"] = transformedPos.z
        return map
    }

    fun forItemStack(stack: ItemStack): MutableMap<String, Any> {
        val map = forItem(stack.item)
        map["tags"] = tagsToList(stack.tags)
        map["count"] = stack.count
        map["maxStackSize"] = stack.maxStackSize
        return map
    }

    fun forItem(item: Item): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["technicalName"] = Registry.ITEM.getKey(item).toString()
        map["name"] = item.description.string
        return map
    }

    fun <T> tagsToList(tags: Stream<TagKey<T>>): List<String> {
        return tags.map { key -> key.location.toString() }.collect(Collectors.toList())
    }
}