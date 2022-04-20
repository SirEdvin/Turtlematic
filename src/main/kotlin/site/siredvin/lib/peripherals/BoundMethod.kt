package site.siredvin.lib.peripherals

import dan200.computercraft.api.lua.IArguments
import dan200.computercraft.api.lua.ILuaContext
import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.peripheral.IComputerAccess
import dan200.computercraft.core.asm.NamedMethod
import dan200.computercraft.core.asm.PeripheralMethod
import java.util.*

class BoundMethod(private val target: Any, method: NamedMethod<PeripheralMethod>) {
    val name: String
    private val method: PeripheralMethod

    init {
        name = method.name
        this.method = method.method
    }

    @Throws(LuaException::class)
    fun apply(
        access: IComputerAccess,
        context: ILuaContext,
        arguments: IArguments
    ): MethodResult {
        return method.apply(target, context, access, arguments)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is BoundMethod) return false
        val that = o
        return target == that.target && name == that.name && method == that.method
    }

    override fun hashCode(): Int {
        return Objects.hash(target, name, method)
    }
}