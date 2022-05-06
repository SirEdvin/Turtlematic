package site.siredvin.turtlematic.api

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.api.peripheral.IBasePeripheral

fun interface AutomataPeripheralBuildFunction<T : IBasePeripheral<*>> {
    fun build(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier): T
}