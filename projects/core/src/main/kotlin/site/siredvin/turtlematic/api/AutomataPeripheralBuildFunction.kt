package site.siredvin.turtlematic.api

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.api.peripheral.IOwnedPeripheral

fun interface AutomataPeripheralBuildFunction<T : IOwnedPeripheral<*>> {
    fun build(turtle: ITurtleAccess, side: TurtleSide, tier: IAutomataCoreTier): T
}