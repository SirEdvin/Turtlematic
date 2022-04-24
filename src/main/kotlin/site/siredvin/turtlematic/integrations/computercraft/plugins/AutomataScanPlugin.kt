package site.siredvin.turtlematic.integrations.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import site.siredvin.turtlematic.integrations.computercraft.peripheral.BaseAutomataCorePeripheral
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.LuaConverter
import site.siredvin.lib.util.ScanUtils
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.math.min

class AutomataScanPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val blockStateConverter: (BlockState, BlockPos, Direction, BlockPos) -> (MutableMap<String, Any>) = {
        state, pos, facing, center -> LuaConverter.withPos(state, pos, facing, center, LuaConverter::blockStateToLua)
    },
    private val entityConverter: (Entity, Direction, BlockPos) -> (MutableMap<String, Any>) = {
        entity, facing, center -> LuaConverter.withPos(entity, facing, center, LuaConverter::entityToLua)
    },
    private val itemConverter: (ItemEntity, Direction, BlockPos) -> (MutableMap<String, Any>) = {
            entity, facing, center -> LuaConverter.withPos(entity, facing, center) { LuaConverter.stackToObject(it.item) }
    },
    private val suitableEntity: Predicate<Entity> = Predicate { false },
    private val allowedMods: Set<AreaInteractionMode> = setOf(AreaInteractionMode.ITEM)
) : AutomataCorePlugin(automataCore) {

    protected fun getBox(pos: BlockPos, radius: Int): AABB {
        val x: Int = pos.x
        val y: Int = pos.y
        val z: Int = pos.z
        val interactionRadius = min(radius, automataCore.interactionRadius)
        return AABB(
            (x - interactionRadius).toDouble(), (y - interactionRadius).toDouble(), (z - interactionRadius).toDouble(),
            (x + interactionRadius).toDouble(), (y + interactionRadius).toDouble(), (z + interactionRadius).toDouble()
        ).inflate(0.99)
    }

    private fun scanBlocks(radius: Int): List<Pair<BlockState, BlockPos>> {
        val owner = automataCore.peripheralOwner
        val result = mutableListOf<Pair<BlockState, BlockPos>>()
        ScanUtils.traverseBlocks(
            owner.level!!, owner.pos, min(radius, automataCore.interactionRadius),
            { state, pos -> result.add(Pair(state, pos))}, relativePosition = false
        )
        return result
    }

    private fun <T: Entity> scanEntities(entityClass: Class<T>, radius: Int): List<T> {
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        return owner.level!!.getEntitiesOfClass(entityClass, getBox(owner.pos, radius))
    }

    private fun scanItems(radius: Int): List<ItemEntity> {
        return scanEntities(ItemEntity::class.java, radius)
    }

    private fun scanLivingEntities(radius: Int): List<LivingEntity> {
        return scanEntities(LivingEntity::class.java, radius)
    }

    @LuaFunction(mainThread = true)
    fun scan(arguments: IArguments): MethodResult {
        val turtle = automataCore.peripheralOwner.turtle
        automataCore.addRotationCycle()
        val mode = AreaInteractionMode.luaValueOf(arguments.getString(0), allowedMods)
        val radius = arguments.optInt(1, automataCore.interactionRadius)
        // TODO: add radius validation
        val center = turtle.position
        return when (mode) {
            AreaInteractionMode.ITEM -> MethodResult.of(
                scanItems(radius).stream().map { itemConverter(it, turtle.direction, center) }.collect(Collectors.toList())
            )
            AreaInteractionMode.ENTITY -> MethodResult.of(
                scanLivingEntities(radius).stream()
                    .filter { suitableEntity.test(it) }.map { entityConverter(it, turtle.direction, center) }.collect(Collectors.toList())
            )
            AreaInteractionMode.BLOCK -> MethodResult.of(
                scanBlocks(radius).stream().map { blockStateConverter(it.first, it.second, turtle.direction, center) }.collect(Collectors.toList())
            )
        }
    }
}