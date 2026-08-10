local automata = assert(peripheral.find("husbandryAutomata"), "Husbandry automata is missing")

local crop = automata.look("block")
assert(crop.name == "minecraft:wheat" and crop.age == 7, "Expected mature wheat")
assert(automata.harvest(), "Expected to harvest wheat")
assert(automata.getHusbandryPoints() > 200, "Expected harvest to award husbandry points")

local animals = automata.scan("entity", 4)
assert(#animals == 1 and animals[1].name == "minecraft:cow", "Expected to scan cow")
local ok, err = automata.capture("entity", "up")
assert(ok, "Expected to capture cow: " .. tostring(err))
assert(automata.getCaptured().name == "minecraft:cow", "Expected captured cow data")
assert(automata.release(), "Expected to release cow")

turtle.select(1)
assert(automata.simulateGrow(), "Expected sapling simulation")
local logs = 0
for slot = 1, 16 do
    local item = turtle.getItemDetail(slot)
    if item and item.name == "minecraft:oak_log" then logs = logs + item.count end
end
assert(logs == 12, "Expected twelve simulated logs")
