local monocle = assert(peripheral.find("inspection_monocle"), "Inspection monocle upgrade is missing")
local block = monocle.inspect("up")

assert(block.name == "minecraft:chest", "Expected to inspect chest")
assert(block.state.facing == "north", "Expected north-facing chest")
assert(block.nbt.Lock == "inspect-test", "Expected chest block entity NBT")
