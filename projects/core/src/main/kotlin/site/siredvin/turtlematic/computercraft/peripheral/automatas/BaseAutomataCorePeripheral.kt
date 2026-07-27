package site.siredvin.turtlematic.computercraft.peripheral.automatas

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.core.BlockPos
import site.siredvin.turtlematic.api.IAutomataCoreTier
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.AutomataCoreFuelBoon
import site.siredvin.turtlematic.computercraft.operations.SingleOperation
import site.siredvin.turtlematic.computercraft.operations.SingleOperationContext
import site.siredvin.turtlematic.util.DataStorageObjects
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralCheck
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralFunction
import site.siredvin.tweakium.modules.peripheral.api.IPeripheralOperation
import site.siredvin.tweakium.modules.peripheral.boon.PeripheralOwnerBoonKey
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner

abstract class BaseAutomataCorePeripheral(
    type: String,
    turtle: ITurtleAccess,
    side: TurtleSide,
    tier: IAutomataCoreTier,
) : OwnedPeripheral<TurtlePeripheralOwner>(
    type,
    TurtlePeripheralOwner(
        turtle,
        side,
    ),
) {
    val tier: IAutomataCoreTier
    private val attributes: MutableMap<String, Boolean> = HashMap()

    init {
        peripheralOwner.attachBoon(PeripheralOwnerBoonKey.FUEL, AutomataCoreFuelBoon(peripheralOwner, tier))
        peripheralOwner.attachOperations(reduceRate = tier.cooldownReduceFactor, cooldownThreshold = TurtlematicConfig.cooldownTresholdLevel)
        peripheralOwner.getBoon(PeripheralOwnerBoonKey.OPERATION).let { ability ->
            possibleOperations().forEach {
                ability?.registerOperation(it)
            }
        }
        this.tier = tier
    }

    @JvmOverloads
    fun addRotationCycle(count: Int = 1) {
        DataStorageObjects.RotationCharge.addCycles(peripheralOwner, count)
    }

    open fun possibleOperations(): MutableList<IPeripheralOperation<*>> = mutableListOf()

    override val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val data = super.peripheralConfiguration
            data["interactionRadius"] = interactionRadius
            return data
        }
    val interactionRadius: Int
        get() = tier.interactionRadius

    fun forUnknownDistance(): SingleOperationContext = SingleOperationContext(1, interactionRadius)

    fun toDistance(target: BlockPos): SingleOperationContext = SingleOperationContext(1, peripheralOwner.pos.distManhattan(target))

    @Throws(LuaException::class)
    fun <T> withOperation(
        operation: IPeripheralOperation<T>,
        context: T,
        function: IPeripheralFunction<T, MethodResult>,
        check: IPeripheralCheck<T>? = null,
    ): MethodResult = peripheralOwner.withOperation(operation, context, function, check, { addRotationCycle() })

    @Throws(LuaException::class)
    fun withOperation(
        operation: SingleOperation,
        function: IPeripheralFunction<SingleOperationContext, MethodResult>,
    ): MethodResult = withOperation(operation, forUnknownDistance(), function, null)

    @Throws(LuaException::class)
    fun withOperation(
        operation: SingleOperation,
        function: IPeripheralFunction<SingleOperationContext, MethodResult>,
        check: IPeripheralCheck<SingleOperationContext>?,
    ): MethodResult = withOperation(operation, forUnknownDistance(), function, check)
}
