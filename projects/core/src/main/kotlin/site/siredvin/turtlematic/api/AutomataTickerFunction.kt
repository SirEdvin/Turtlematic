package site.siredvin.turtlematic.api

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide

fun interface AutomataTickerFunction {
    fun tick(turtle: ITurtleAccess, side: TurtleSide, coreTier: IAutomataCoreTier, tickCounter: Long)
}
