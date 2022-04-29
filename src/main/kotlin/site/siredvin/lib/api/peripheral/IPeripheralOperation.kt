package site.siredvin.lib.api.peripheral

import site.siredvin.lib.api.IConfigHandler

interface IPeripheralOperation<T> : IConfigHandler {
    val initialCooldown: Int
    fun getCooldown(context: T): Int
    fun getCost(context: T): Int
    fun computerDescription(): Map<String, Any?>
}