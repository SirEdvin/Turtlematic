package site.siredvin.lib.api

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.lib.api.peripheral.IBasePeripheral

fun interface TurtlePeripheralBuildFunction<T : IBasePeripheral<*>> {
    fun build(turtle: ITurtleAccess, side: TurtleSide): T
}