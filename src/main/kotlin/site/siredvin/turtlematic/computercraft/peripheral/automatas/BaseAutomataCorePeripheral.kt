package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.lib.api.peripheral.IPeripheralCheck
import site.siredvin.lib.api.peripheral.IPeripheralFunction
import site.siredvin.lib.api.peripheral.IPeripheralOperation
import site.siredvin.lib.computercraft.peripheral.BasePeripheral
import site.siredvin.turtlematic.computercraft.operations.SingleOperationContext
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.util.DataStorageUtil
import site.siredvin.turtlematic.computercraft.operations.SingleOperation

abstract class BaseAutomataCorePeripheral(
    type: String,
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier
) : BasePeripheral<TurtlePeripheralOwner>(
    type, TurtlePeripheralOwner(
        turtle, side
    )
) {
    val tier: IAutomataCoreTier
    private val attributes: MutableMap<String, Boolean> = HashMap()

    init {
        peripheralOwner.attachFuel(tier.maxFuelConsumptionRate)
        @Suppress("LeakingThis")
        peripheralOwner.attachOperation(possibleOperations(), tier.cooldownReduceFactor)
        this.tier = tier
    }

    @JvmOverloads
    fun addRotationCycle(count: Int = 1) {
        DataStorageUtil.RotationCharge.addCycles(peripheralOwner, count)
    }

    open fun possibleOperations(): MutableList<IPeripheralOperation<*>> {
        return ArrayList()
    }

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val data = super.peripheralConfiguration
            data["interactionRadius"] = interactionRadius
            return data
        }
    val interactionRadius: Int
        get() = tier.interactionRadius

    fun forUnknownDistance(): SingleOperationContext {
        return SingleOperationContext(1, interactionRadius)
    }

    fun toDistance(target: BlockPos): SingleOperationContext {
        return SingleOperationContext(1, pos.distManhattan(target))
    }

    @Throws(LuaException::class)
    fun <T> withOperation(
        operation: IPeripheralOperation<T>,
        context: T,
        function: IPeripheralFunction<T, MethodResult>,
        check: IPeripheralCheck<T>? = null
    ): MethodResult {
        return withOperation(operation, context, check, function) { addRotationCycle() }
    }

    @Throws(LuaException::class)
    fun withOperation(
        operation: SingleOperation,
        function: IPeripheralFunction<SingleOperationContext, MethodResult>
    ): MethodResult {
        return withOperation(operation, forUnknownDistance(), function, null)
    }

    @Throws(LuaException::class)
    fun withOperation(
        operation: SingleOperation,
        function: IPeripheralFunction<SingleOperationContext, MethodResult>,
        check: IPeripheralCheck<SingleOperationContext>?
    ): MethodResult {
        return withOperation(operation, forUnknownDistance(), function, check)
    }
}