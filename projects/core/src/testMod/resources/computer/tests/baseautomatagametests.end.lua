local automata = assert(peripheral.find("endAutomata"), "End automata is missing")

assert(automata.capture("block"), "Expected to capture chest")
assert(automata.getCaptured().name == "minecraft:chest", "Expected captured chest data")

assert(automata.savePoint("home"), "Expected to save warp point")
assert(#automata.points() == 1, "Expected one warp point")
assert(turtle.forward(), "Expected turtle to move")
assert(automata.distanceToPoint("home") == 1, "Expected one block distance")
assert(automata.estimateWarpCost("home") > 0, "Expected positive warp cost")
assert(automata.warpToPoint("home"), "Expected to warp home")
assert(automata.release(), "Expected to release chest after warp")
assert(automata.deletePoint("home"), "Expected to delete warp point")
assert(#automata.points() == 0, "Expected no warp points")
