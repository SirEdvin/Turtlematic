package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.lib.util.representation.LuaRepresentation
import site.siredvin.turtlematic.computercraft.datatypes.InteractionMode
import site.siredvin.turtlematic.computercraft.datatypes.VerticalDirection
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import kotlin.reflect.KFunction2

class AutomataLookPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val entityEnriches: List<KFunction2<Entity, MutableMap<String, Any>, Unit>> = emptyList(),
    private val blockStateEnriches: List<KFunction2<BlockState, MutableMap<String, Any>, Unit>> = emptyList(),
    private val blockEntityEnriches: List<KFunction2<BlockEntity, MutableMap<String, Any>, Unit>> = emptyList(),
    private val allowedMods: Set<InteractionMode> = InteractionMode.values().toSet()
) : AutomataCorePlugin(automataCore) {

    private fun entityConverter(entity: Entity): MutableMap<String, Any> {
        val base = LuaRepresentation.forEntity(entity)
        entityEnriches.forEach { it.invoke(entity, base) }
        return base
    }

    private fun blockStateConverter(blockState: BlockState): MutableMap<String, Any> {
        val base = LuaRepresentation.forBlockState(blockState)
        blockStateEnriches.forEach { it.invoke(blockState, base) }
        return base
    }

    private fun blockEntityConverter(blockEntity: BlockEntity, data: MutableMap<String, Any>) {
        blockEntityEnriches.forEach { it.invoke(blockEntity, data) }
    }

    private fun lookImpl(arguments: IArguments): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0), allowedMods)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) null else VerticalDirection.luaValueOf(
            directionArgument.get()
        )
        if (!allowedMods.contains(mode))
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
                APFakePlayer -> APFakePlayer.findHit(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock)
        }, overwrittenDirection=overwrittenDirection?.minecraftDirection)
        if (result.type == HitResult.Type.MISS)
            return MethodResult.of(null, "Nothing found")
        if (result is BlockHitResult) {
            val base = blockStateConverter(owner.level!!.getBlockState(result.blockPos))
            val entity = owner.level!!.getBlockEntity(result.blockPos)
            if (entity != null)
                blockEntityConverter(entity, base)
            return MethodResult.of(base)
        }
        if (result is EntityHitResult)
            return MethodResult.of(entityConverter(result.entity))
        return MethodResult.of(null, "Nothing found")
    }

    @LuaFunction(mainThread = true)
    fun look(arguments: IArguments): MethodResult {
        return lookImpl(arguments)
    }
}