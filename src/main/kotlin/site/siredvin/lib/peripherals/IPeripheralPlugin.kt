package site.siredvin.lib.peripherals

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.core.asm.NamedMethod
import dan200.computercraft.core.asm.PeripheralMethod
import java.util.stream.Collectors

interface IPeripheralPlugin {
    val methods: List<BoundMethod>
        get() = PeripheralMethod.GENERATOR.getMethods(this.javaClass).stream()
            .map { named: NamedMethod<PeripheralMethod> -> BoundMethod(this, named) }
            .collect(Collectors.toList())
    // TODO: consider something
    open val operations: Array<IPeripheralOperation<*>>
        get() = emptyArray()

    fun isSuitable(peripheral: IPeripheral): Boolean {
        return true
    }
}