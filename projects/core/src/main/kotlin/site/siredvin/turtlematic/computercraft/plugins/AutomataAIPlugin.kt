package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import java.util.function.Predicate

class AutomataAIPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
) : AutomataCorePlugin(automataCore) {

    private fun toggleEntityAI(hit: EntityHitResult): MethodResult {
        if (hit.entity !is Mob) {
            return MethodResult.of(false, "Entity doesn't have AI")
        }
        val mob = hit.entity as Mob
        mob.isNoAi = !mob.isNoAi
        return MethodResult.of(true)
    }

    private fun isEntityAIEnabled(hit: EntityHitResult): Boolean? {
        if (hit.entity !is Mob) {
            return null
        }
        val mob = hit.entity as Mob
        return !mob.isNoAi
    }

    @LuaFunction(mainThread = true)
    fun toggleAI(arguments: IArguments): MethodResult {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val hit = automataCore.peripheralOwner.withPlayer({
            it.findHit(
                skipEntity = false,
                skipBlock = true,
                entityFilter = suitableEntity,
            )
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return when (hit.type) {
            HitResult.Type.MISS -> MethodResult.of(null, "nothing found")
            HitResult.Type.BLOCK -> MethodResult.of(null, "nothing found")
            HitResult.Type.ENTITY -> toggleEntityAI(hit as EntityHitResult)
            else -> throw LuaException("This should never, never happen at all")
        }
    }

    @LuaFunction(mainThread = true)
    fun isAIEnabled(arguments: IArguments): Boolean? {
        val directionArgument = arguments.optString(0)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        val hit = automataCore.peripheralOwner.withPlayer({
            it.findHit(
                skipEntity = false,
                skipBlock = true,
                entityFilter = suitableEntity,
            )
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return when (hit.type) {
            HitResult.Type.MISS -> null
            HitResult.Type.BLOCK -> null
            HitResult.Type.ENTITY -> isEntityAIEnabled(hit as EntityHitResult)
            else -> throw LuaException("This should never, never happen at all")
        }
    }
}
