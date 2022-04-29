package site.siredvin.lib.api.peripheral

import dan200.computercraft.api.lua.MethodResult

fun interface IPeripheralCheck<T> {
    class ChainedPeripheralCheck<T1>(
        private val first: IPeripheralCheck<T1>,
        private val second: IPeripheralCheck<T1>
    ) : IPeripheralCheck<T1> {
        override fun check(context: T1): MethodResult? {
            val firstCheck = first.check(context)
            return firstCheck ?: second.check(context)
        }
    }

    fun check(context: T): MethodResult?
    fun checkAlso(check: IPeripheralCheck<T>): IPeripheralCheck<T>? {
        return ChainedPeripheralCheck(this, check)
    }
}