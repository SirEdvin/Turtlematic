package site.siredvin.lib.computercraft.peripheral

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BoundMethod) return false
        return target == other.target && name == other.name && method == other.method
    }

    override fun hashCode(): Int {
        return Objects.hash(target, name, method)
    }
}