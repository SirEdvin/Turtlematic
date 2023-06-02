package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.api.datatypes.InteractionMode
import site.siredvin.peripheralium.api.datatypes.VerticalDirection
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import java.util.function.Predicate

class AutomataInteractionPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
    private val allowedMods: Set<InteractionMode> = setOf(InteractionMode.BLOCK),
) : AutomataCorePlugin(automataCore) {
    override val operations: List<IPeripheralOperation<*>>
        get() = listOf(SingleOperation.SWING, SingleOperation.USE)

    fun swingImplInner(arguments: IArguments): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0), allowedMods)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        if (!allowedMods.contains(mode)) {
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        }
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
            it.swing(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity)
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        if (!result.left) {
            return MethodResult.of(null, result.right)
        }
        return MethodResult.of(true)
    }

    fun swingImpl(arguments: IArguments): MethodResult {
        return automataCore.withOperation(SingleOperation.SWING) {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            val selectedTool: ItemStack = owner.toolInMainHand
            val previousDamageValue = selectedTool.damageValue
            val result = swingImplInner(arguments)
            if (automataCore.tier.needRestoreDurability()) {
                selectedTool.damageValue = previousDamageValue
            }
            result
        }
    }

    fun useImplInner(arguments: IArguments): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0), allowedMods)
        val directionArgument = arguments.optString(1)
        val overwrittenDirection = if (directionArgument.isEmpty) {
            null
        } else {
            VerticalDirection.luaValueOf(
                directionArgument.get(),
            )
        }
        if (!allowedMods.contains(mode)) {
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        }
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
            it.use(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity)
        }, overwrittenDirection = overwrittenDirection?.minecraftDirection)
        return MethodResult.of(true, result.toString())
    }

    fun useImpl(arguments: IArguments): MethodResult {
        return automataCore.withOperation(SingleOperation.USE) {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            val selectedTool: ItemStack = owner.toolInMainHand
            val previousDamageValue = selectedTool.damageValue
            val result = useImplInner(arguments)
            if (automataCore.tier.needRestoreDurability()) {
                selectedTool.damageValue = previousDamageValue
            }
            result
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun swing(arguments: IArguments): MethodResult {
        return swingImpl(arguments)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun use(arguments: IArguments): MethodResult {
        return useImpl(arguments)
    }
}
