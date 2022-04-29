package site.siredvin.turtlematic.computercraft.plugins

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
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.lib.util.world.ScanUtils
import site.siredvin.lib.util.assertBetween
import site.siredvin.lib.util.representation.LuaRepresentation
import site.siredvin.turtlematic.computercraft.datatypes.AreaInteractionMode
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.math.min
import kotlin.reflect.KFunction2

class AutomataScanPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val entityEnriches: List<KFunction2<Entity, MutableMap<String, Any>, Unit>> = emptyList(),
    private val blockStateEnriches: List<KFunction2<BlockState, MutableMap<String, Any>, Unit>> = emptyList(),
    private val itemEnriches: List<KFunction2<ItemEntity, MutableMap<String, Any>, Unit>> = emptyList(),
    private val suitableEntity: Predicate<Entity> = Predicate { false },
    private val allowedMods: Set<AreaInteractionMode> = setOf(AreaInteractionMode.ITEM)
) : AutomataCorePlugin(automataCore) {

    private fun entityConverter(entity: Entity, facing: Direction, center: BlockPos): MutableMap<String, Any> {
        val base = LuaRepresentation.withPos(entity, facing, center, LuaRepresentation::forEntity)
        entityEnriches.forEach { it.invoke(entity, base) }
        return base
    }

    private fun blockStateConverter(state: BlockState, pos: BlockPos, facing: Direction, center: BlockPos): MutableMap<String, Any> {
        val base = LuaRepresentation.withPos(state, pos, facing, center, LuaRepresentation::forBlockState)
        blockStateEnriches.forEach { it.invoke(state, base) }
        return base
    }

    private fun itemConverter(entity: ItemEntity, facing: Direction, center: BlockPos): MutableMap<String, Any> {
        val base = LuaRepresentation.withPos(entity, facing, center) { LuaRepresentation.forItemStack(it.item) }
        itemEnriches.forEach { it.invoke(entity, base) }
        return base
    }

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
        assertBetween(radius, 1, automataCore.interactionRadius, "radius")
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