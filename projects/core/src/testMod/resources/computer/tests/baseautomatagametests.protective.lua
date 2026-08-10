local automata = assert(peripheral.find("protectiveAutomata"), "Protective automata is missing")

local threats = automata.scan("entity", 4)
assert(#threats == 1 and threats[1].name == "minecraft:zombie", "Expected only hostile mob scan")
local target = automata.look("entity", "up")
assert(target and target.name == "minecraft:zombie", "Expected to look at zombie")

turtle.select(1)
local ok, err = automata.swing("entity", "up")
assert(ok, "Expected to attack zombie: " .. tostring(err))
ok, err = automata.capture("entity", "up")
assert(ok, "Expected to capture zombie: " .. tostring(err))
assert(automata.getCaptured().name == "minecraft:zombie", "Expected captured zombie data")
assert(automata.release(), "Expected to release zombie")
