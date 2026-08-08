local piston = assert(peripheral.find("piston"), "Piston upgrade is missing")

local present, block = turtle.inspect()
assert(present and block.name == "minecraft:stone", "Expected stone in front of turtle")
assert(piston.push(), "Expected piston push to succeed")
assert(not turtle.inspect(), "Expected pushed block to leave the front space")

local ok, err = piston.push("up")
assert(ok == nil and err ~= nil, "Expected bedrock push to fail")
