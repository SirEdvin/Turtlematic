package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.util.world.FakePlayerProxy
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.forged.ExperienceAutomataCorePeripheral
import java.util.*
import java.util.function.Predicate

class AutomataRestockPlugin(
    automataCore: ExperienceAutomataCorePeripheral,
    private val refresher: (Entity) -> MethodResult,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
) : AutomataCorePlugin(automataCore) {

    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.RESTOCK)

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun restock(arguments: IArguments): MethodResult {
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
            HitResult.Type.MISS -> MethodResult.of(null, "No merchant found")
            HitResult.Type.BLOCK -> MethodResult.of(null, "No merchant found")
            HitResult.Type.ENTITY -> {
                return automataCore.withOperation(
                    SingleOperation.RESTOCK,
                ) {
                    val hitEntity: Entity = (hit as EntityHitResult).entity
                    return@withOperation refresher(hitEntity)
                }
            }
            else -> throw LuaException("This should never, never happen at all")
        }
    }
}
