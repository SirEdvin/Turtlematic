local chest = assert(peripheral.find("creative_chest"), "Creative chest upgrade is missing")

assert(chest.generate("minecraft:stone", 3, "{display:{Name:'{\"text\":\"Generated\"}'}}"))
local detail = assert(turtle.getItemDetail(1, true), "Generated item is missing")
assert(detail.name == "minecraft:stone" and detail.count == 3, "Expected three generated stone")
assert(detail.displayName == "Generated", "Expected generated item NBT")

local ok, err = chest.generate("minecraft:missing", 1)
assert(ok == nil and err ~= nil, "Expected unknown item to fail")
assert(turtle.getItemCount(1) == 3, "Invalid generation changed inventory")
