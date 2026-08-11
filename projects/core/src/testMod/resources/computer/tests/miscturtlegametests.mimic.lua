local mimic = assert(peripheral.find("mimic"), "Mimic upgrade is missing")

local ok, err = pcall(mimic.setMimic, { block = "minecraft:not_a_block" })
assert(not ok, "Expected unknown mimic block to fail")
assert(tostring(err):find("Cannot find block minecraft:not_a_block", 1, true), "Unexpected mimic error: " .. tostring(err))

mimic.setMimic({ block = "minecraft:chest", attrs = { facing = "north" } }, "{Lock:\"mimic-test\"}")
local state, nbt = mimic.getMimic()
assert(state.name == "minecraft:chest", "Expected chest mimic")
assert(state.state.facing == "north", "Expected north-facing mimic")
assert(nbt.Lock == "mimic-test", "Expected mimic NBT")

mimic.setTransformation("")
assert(mimic.getTransformation() == "")
assert(mimic.reset())
state, nbt = mimic.getMimic()
assert(state == nil and nbt == nil and mimic.getTransformation() == nil, "Expected reset to clear mimic state")
