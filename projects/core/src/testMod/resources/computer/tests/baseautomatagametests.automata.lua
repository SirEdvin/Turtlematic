local automata = assert(peripheral.find("automata"), "Automata core is missing")
local config = automata.getConfiguration()
assert(config.interactionRadius == 2 and config.maxRadius == 2, "Expected tier-one radii")

local block = automata.look("block")
assert(block.name == "minecraft:chest", "Expected to look at chest")
assert(automata.use("block"), "Expected to use chest")

local items = automata.scan("item", 2)
assert(#items == 1 and items[1].name == "minecraft:apple" and items[1].count == 3, "Expected to scan apples")
assert(automata.suck(2, "minecraft:apple"), "Expected to suck two apples")
assert(turtle.getItemCount(1) == 2, "Expected two apples in turtle inventory")

turtle.select(2)
assert(automata.swing("block"), "Expected to break chest")
