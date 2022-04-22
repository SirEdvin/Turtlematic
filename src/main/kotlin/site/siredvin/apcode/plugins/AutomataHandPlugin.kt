package site.siredvin.apcode.plugins

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import site.siredvin.lib.operations.SingleOperation
import site.siredvin.lib.peripherals.BaseAutomataCorePeripheral
import site.siredvin.lib.peripherals.IPeripheralOperation
import site.siredvin.lib.peripherals.owner.TurtlePeripheralOwner
import java.util.function.Predicate

class AutomataHandPlugin(
    automataCore: BaseAutomataCorePeripheral,
    private val suitableEntity: Predicate<Entity> = Predicate { false },
    private val allowedMods: Set<InteractionMode> = setOf(InteractionMode.BLOCK)
) : AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.SWING, SingleOperation.USE)

    fun swingImplInner(arguments: IArguments, overwrittenDirection: Direction? = null): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0))
        if (!allowedMods.contains(mode))
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
                APFakePlayer -> APFakePlayer.swing(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity)
        }, overwrittenDirection=overwrittenDirection)
        if (!result.left) {
            return MethodResult.of(null, result.right)
        }
        return MethodResult.of(true)
    }

    fun swingImpl(arguments: IArguments, overwrittenDirection: Direction? = null): MethodResult {
        return automataCore.withOperation(SingleOperation.SWING) {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            val selectedTool: ItemStack = owner.toolInMainHand
            val previousDamageValue = selectedTool.damageValue
            val result = swingImplInner(arguments, overwrittenDirection=overwrittenDirection)
            if (automataCore.hasAttribute(BaseAutomataCorePeripheral.ATTR_STORING_TOOL_DURABILITY)) selectedTool.damageValue =
                previousDamageValue
            result
        }
    }

    fun useImplInner(arguments: IArguments, overwrittenDirection: Direction? = null): MethodResult {
        val mode = InteractionMode.luaValueOf(arguments.getString(0))
        if (!allowedMods.contains(mode))
            return MethodResult.of(null, "Mode $mode are not allowed for this core")
        automataCore.addRotationCycle()
        val owner = automataCore.peripheralOwner
        val result = owner.withPlayer({
                APFakePlayer -> APFakePlayer.use(skipEntity = mode.skipEntry, skipBlock = mode.skipBlock, entityFilter = suitableEntity)
        }, overwrittenDirection=overwrittenDirection)
        return MethodResult.of(true, result.toString())
    }

    fun useImpl(arguments: IArguments, overwrittenDirection: Direction? = null): MethodResult {
        return automataCore.withOperation(SingleOperation.USE) {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            val selectedTool: ItemStack = owner.toolInMainHand
            val previousDamageValue = selectedTool.damageValue
            val result = useImplInner(arguments, overwrittenDirection = overwrittenDirection)
            if (automataCore.hasAttribute(BaseAutomataCorePeripheral.ATTR_STORING_TOOL_DURABILITY)) selectedTool.damageValue =
                previousDamageValue
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
    fun swingDown(arguments: IArguments): MethodResult {
        return swingImpl(arguments, overwrittenDirection = Direction.DOWN)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun swingUp(arguments: IArguments): MethodResult {
        return swingImpl(arguments, overwrittenDirection = Direction.UP)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun use(arguments: IArguments): MethodResult {
        return useImpl(arguments)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun useDown(arguments: IArguments): MethodResult {
        return useImpl(arguments, overwrittenDirection = Direction.DOWN)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun useUp(arguments: IArguments): MethodResult {
        return useImpl(arguments, overwrittenDirection = Direction.UP)
    }
}