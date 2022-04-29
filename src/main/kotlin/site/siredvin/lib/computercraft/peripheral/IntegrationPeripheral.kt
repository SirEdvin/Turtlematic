package site.siredvin.lib.computercraft.peripheral

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.ILuaContext
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.api.peripheral.IDynamicPeripheral
import dan200.computercraft.api.peripheral.IPeripheral
import site.siredvin.lib.api.peripheral.IPeripheralPlugin
import java.util.*
import java.util.function.Consumer

abstract class IntegrationPeripheral : IDynamicPeripheral {
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
            _methodNames = pluggedMethods.stream().map { obj: BoundMethod -> obj.name }.toArray { size -> Array(size) { "" } }
        }
    }

    protected fun addPlugin(plugin: IPeripheralPlugin) {
        if (plugins == null) plugins = LinkedList()
        plugins!!.add(plugin)
        val operations = plugin.operations
        require(operations.isEmpty()) { "This is not possible to attach plugin with operations to not operationable owner" }
    }

    fun getConnectedComputers(): List<IComputerAccess> {
        return _connectedComputers
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
}