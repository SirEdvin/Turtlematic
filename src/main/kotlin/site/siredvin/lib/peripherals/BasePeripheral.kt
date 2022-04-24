package site.siredvin.lib.peripherals

import dan200.computercraft.api.lua.*
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IDynamicPeripheral
import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import site.siredvin.lib.peripherals.owner.IOwnerAbility
import site.siredvin.lib.peripherals.owner.IPeripheralOwner
import site.siredvin.lib.peripherals.owner.OperationAbility.FailReason
import site.siredvin.lib.peripherals.owner.PeripheralOwnerAbility
import site.siredvin.lib.util.LuaConverter
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

abstract class BasePeripheral<O : IPeripheralOwner?>(protected val peripheralType: String, final override val peripheralOwner: O) :
    IBasePeripheral<O>, IDynamicPeripheral {
    protected val _connectedComputers: MutableList<IComputerAccess> = ArrayList()
    protected var initialized = false
    protected val pluggedMethods: MutableList<BoundMethod> = ArrayList()
    protected var plugins: MutableList<IPeripheralPlugin>? = null
    protected var _methodNames = Array(0) { "" }
    protected fun buildPlugins() {
        if (!initialized) {
            initialized = true
            pluggedMethods.clear()
            if (plugins != null) plugins!!.forEach(Consumer { plugin: IPeripheralPlugin ->
                if (plugin.isSuitable(this)) pluggedMethods.addAll(
                    plugin.methods
                )
            })
            peripheralOwner!!.abilities.forEach(Consumer { ability: IOwnerAbility? ->
                if (ability is IPeripheralPlugin) pluggedMethods.addAll(
                    (ability as IPeripheralPlugin).methods
                )
            })
            _methodNames = pluggedMethods.stream().map { obj: BoundMethod -> obj.name }.toArray { size -> Array(size) { "" } }
        }
    }

    override val connectedComputers: List<IComputerAccess>
        get() = _connectedComputers

    protected fun addPlugin(plugin: IPeripheralPlugin) {
        if (plugins == null) plugins = LinkedList()
        plugins!!.add(plugin)
        val operations = plugin.operations
        if (operations.isNotEmpty()) {
            val operationAbility = peripheralOwner!!.getAbility(PeripheralOwnerAbility.OPERATION)
                ?: throw IllegalArgumentException("This is not possible to attach plugin with operations to not operationable owner")
            for (operation in operations) operationAbility.registerOperation(operation)
        }
    }

    override fun getTarget(): Any? {
        return peripheralOwner
    }

    override fun getType(): String {
        return peripheralType
    }

    override fun attach(computer: IComputerAccess) {
        _connectedComputers.add(computer)
    }

    override fun detach(computer: IComputerAccess) {
        _connectedComputers.remove(computer)
    }

    override fun equals(iPeripheral: IPeripheral?): Boolean {
        return iPeripheral === this
    }

    open val peripheralConfiguration: MutableMap<String?, Any?>?
        get() {
            val data: MutableMap<String?, Any?> = HashMap()
            peripheralOwner!!.abilities.forEach(Consumer { ability: IOwnerAbility -> ability.collectConfiguration(data) })
            return data
        }

    @get:LuaFunction
    val configuration: Map<String?, Any?>?
        get() = peripheralConfiguration
    protected val pos: BlockPos
        protected get() = peripheralOwner!!.pos
    protected val level: Level?
        protected get() = peripheralOwner!!.level

    @Throws(LuaException::class)
    protected fun validateSide(direction: String): Direction {
        val dir = direction.uppercase()
        return LuaConverter.getDirection(peripheralOwner!!.facing, dir)
    }

    override fun getMethodNames(): Array<String> {
        if (!initialized) buildPlugins()
        return _methodNames
    }

    @Throws(LuaException::class)
    override fun callMethod(
        access: IComputerAccess,
        context: ILuaContext,
        index: Int,
        arguments: IArguments
    ): MethodResult {
        if (!initialized) buildPlugins()
        return pluggedMethods[index]!!.apply(access, context, arguments)
    }

    @Throws(LuaException::class)
    protected fun <T> withOperation(
        operation: IPeripheralOperation<T>,
        context: T,
        check: IPeripheralCheck<T>?,
        method: IPeripheralFunction<T, MethodResult>,
        successCallback: Consumer<T>?
    ): MethodResult {
        return withOperation(operation, context, check, method, successCallback, null)
    }

    @Throws(LuaException::class)
    protected fun <T> withOperation(
        operation: IPeripheralOperation<T>,
        context: T,
        check: IPeripheralCheck<T>?,
        method: IPeripheralFunction<T, MethodResult>,
        successCallback: Consumer<T>?,
        failCallback: BiConsumer<MethodResult?, FailReason?>?
    ): MethodResult {
        val operationAbility = peripheralOwner!!.getAbility(PeripheralOwnerAbility.OPERATION)
            ?: throw IllegalArgumentException("This shouldn't happen at all")
        return operationAbility.performOperation(operation, context, check, method, successCallback, failCallback)
    }
}