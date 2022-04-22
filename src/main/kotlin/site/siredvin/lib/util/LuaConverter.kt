package site.siredvin.lib.util

import com.mojang.math.Matrix4f
import com.mojang.math.Transformation
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.core.computer.ComputerSide
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

object LuaConverter {

    fun blockStateToLua(state: BlockState): MutableMap<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        val blockName = state.block.builtInRegistryHolder().key().registry()
        if (blockName != null) data["name"] = blockName.toString()
        data["tags"] = tagsToList(state.tags)
        return data
    }

    fun entityToLua(entity: Entity): MutableMap<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["id"] = entity.id
        data["uuid"] = entity.stringUUID
        data["name"] = entity.name.string
        data["tags"] = entity.tags
        return data
    }

    fun <T: Entity> withPos(entity: T, facing: Direction, center: BlockPos, converter: (T) -> (MutableMap<String, Any>)):  MutableMap<String, Any> {
        val base = converter(entity)
        base.putAll(posToObject(entity.blockPosition(), facing, center))
        return base
    }

    fun <T> withPos(value: T, pos: BlockPos, facing: Direction, center: BlockPos, converter: (T) -> (MutableMap<String, Any>)):  MutableMap<String, Any> {
        val base = converter(value)
        base.putAll(posToObject(pos, facing, center))
        return base
    }

    fun animalToLua(animal: Animal): MutableMap<String, Any> {
        val data = entityToLua(animal);
        data["baby"] = animal.isBaby
        data["inLove"] = animal.isInLove
        data["aggressive"] = animal.isAggressive
        if (animal is Shearable) {
            data["shareable"] = animal.readyForShearing();
        }
        return data;
    }

    fun completeEntityToLua(entity: Entity): MutableMap<String, Any> {
        return if (entity is Animal) animalToLua(entity) else entityToLua(entity)
    }

    fun posToObject(pos: BlockPos, facing: Direction, center: BlockPos): MutableMap<String, Any> {
        val transformedPos = pos.subtract(center).toRelative(facing)
        val map: MutableMap<String, Any> = HashMap()
        map["x"] = transformedPos.x
        map["y"] = transformedPos.y
        map["z"] = transformedPos.z
        return map
    }

    fun stackToObject(stack: ItemStack): MutableMap<String, Any> {
        val map = itemToObject(stack.item)
        map["tags"] = tagsToList(stack.tags)
        map["count"] = stack.count
        map["name"] = stack.displayName.toString()
        map["maxStackSize"] = stack.maxStackSize
        return map
    }

    fun itemToObject(item: Item): MutableMap<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        map["technicalName"] = Registry.ITEM.getKey(item).toString()
        return map
    }

    fun <T> tagsToList(tags: Stream<TagKey<T>>): List<String> {
        return tags.map { key -> key.location.toString() }.collect(Collectors.toList())
    }

    @Throws(LuaException::class)
    fun getDirection(facing: Direction, computerSide: String): Direction {
        if (Direction.byName(computerSide) != null) return Direction.byName(computerSide)!!
        if (computerSide == ComputerSide.FRONT.toString()) return facing
        if (computerSide == ComputerSide.BACK.toString()) return facing.opposite
        if (computerSide == ComputerSide.TOP.toString()) return Direction.UP
        if (computerSide == ComputerSide.BOTTOM.toString()) return Direction.DOWN
        if (computerSide == ComputerSide.RIGHT.toString()) return facing.counterClockWise
        if (computerSide == ComputerSide.LEFT.toString()) return facing.clockWise
        throw LuaException("$computerSide is not a valid side")
    }

    // BlockPos tricks
    @Throws(LuaException::class)
    fun convertToBlockPos(table: Map<*, *>): BlockPos {
        if (!table.containsKey("x") || !table.containsKey("y") || !table.containsKey("z")) throw LuaException("Table should be block position table")
        val x = table["x"]
        val y = table["y"]
        val z = table["z"]
        if (x !is Number || y !is Number || z !is Number) throw LuaException("Table should be block position table")
        return BlockPos(x.toInt(), y.toInt(), z.toInt())
    }

    @Throws(LuaException::class)
    fun convertToBlockPos(center: BlockPos, table: Map<*, *>): BlockPos {
        val relative = convertToBlockPos(table)
        return BlockPos(center.x + relative.x, center.y + relative.y, center.z + relative.z)
    }
}