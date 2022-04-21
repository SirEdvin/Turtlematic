package site.siredvin.turtlematic.common.items

import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.common.items.base.TurtleItem
import site.siredvin.turtlematic.integrations.computercraft.turtle.Automata

class AutomataCore : TurtleItem(Automata.ID, {TurtlematicConfig.enableAutomataCore}){
}