package site.siredvin.lib.computercraft.peripheral

import dan200.computercraft.api.lua.*
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IDynamicPeripheral
import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import site.siredvin.lib.api.peripheral.*
import site.siredvin.lib.computercraft.peripheral.ability.OperationAbility
import site.siredvin.lib.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.lib.util.OrientationUtil
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
    protected var additionalTypeStorage: MutableSet<String>? = null

    protected fun connectPlugin(plugin: IPeripheralPlugin) {
        if (plugin.isSuitable(this)) {
            pluggedMethods.addAll(plugin.methods)
            val additionalType = plugin.additionalType
            if (additionalType != null) {
                if (additionalTypeStorage == null)
                    additionalTypeStorage = mutableSetOf()
                additionalTypeStorage!!.add(additionalType)
            }
        }
    }

    protected fun buildPlugins() {
        if (!initialized) {
            initialized = true
            pluggedMethods.clear()
            additionalTypeStorage?.clear()
            if (plugins != null) plugins!!.forEach(this::connectPlugin)
            peripheralOwner!!.abilities.forEach(Consumer { ability: IOwnerAbility? ->
                if (ability is IPeripheralPlugin) {
                    this.connectPlugin(ability)
                }
            })
            _methodNames = pluggedMethods.stream().map { obj: BoundMethod -> obj.name }.toArray { size -> Array(size) { "" } }
        }
    }

    override val connectedComputers: List<IComputerAccess>
        get() = _connectedComputers

    override fun getAdditionalTypes(): MutableSet<String> {
        if (!initialized) buildPlugins()
        return additionalTypeStorage ?: Collections.emptySet()
    }

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

    open val peripheralConfiguration: MutableMap<String, Any>
        get() {
            val data: MutableMap<String, Any> = HashMap()
            peripheralOwner!!.abilities.forEach(Consumer { ability: IOwnerAbility -> ability.collectConfiguration(data) })
            return data
        }

    @get:LuaFunction
    val configuration: Map<String, Any>
        get() = peripheralConfiguration
    protected val pos: BlockPos
        get() = peripheralOwner!!.pos
    protected val level: Level?
        get() = peripheralOwner!!.level

    @Throws(LuaException::class)
    protected fun validateSide(direction: String): Direction {
        val dir = direction.uppercase()
        return OrientationUtil.getDirection(peripheralOwner!!.facing, dir)
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
        return pluggedMethods[index].apply(access, context, arguments)
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
        failCallback: BiConsumer<MethodResult?, OperationAbility.FailReason?>?
    ): MethodResult {
        val operationAbility = peripheralOwner!!.getAbility(PeripheralOwnerAbility.OPERATION)
            ?: throw IllegalArgumentException("This shouldn't happen at all")
        return operationAbility.performOperation(operation, context, check, method, successCallback, failCallback)
    }
}