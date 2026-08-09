local automata = assert(peripheral.find("enormousAutomata"), "Enormous automata is missing")
local methods = automata.getConfiguration().scanMethods
local available = {}
for _, method in ipairs(methods) do available[method] = true end
assert(available.block and available.entity and available.item, "Expected all scan modes")

local blocks = automata.scan("block", 2, "minecraft:gold_block")
assert(#blocks == 1 and blocks[1].name == "minecraft:gold_block", "Expected gold block scan")
local entities = automata.scan("entity", 2)
assert(#entities == 1 and entities[1].name == "minecraft:armor_stand", "Expected unrestricted entity scan")
local items = automata.scan("item", 2)
assert(#items == 1 and items[1].name == "minecraft:diamond", "Expected item scan")

assert(automata.savePoint("home"), "Expected warping API")
assert(automata.capture("block"), "Expected unrestricted block capture")
assert(automata.release(), "Expected unrestricted block release")
