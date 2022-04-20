package site.siredvin.lib.peripherals

import site.siredvin.lib.misc.IConfigHandler

interface IPeripheralOperation<T> : IConfigHandler {
    val initialCooldown: Int
    fun getCooldown(context: T): Int
    fun getCost(context: T): Int
    fun computerDescription(): Map<String, Any?>
}