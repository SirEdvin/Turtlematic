local piston = assert(peripheral.find("sticky_piston"), "Sticky piston upgrade is missing")

assert(piston.push(), "Expected sticky piston push to succeed")
sleep(0.2)
assert(not turtle.inspect(), "Expected pushed block to leave the front space")
assert(piston.pull(), "Expected sticky piston pull to succeed")
sleep(0.2)
local present, block = turtle.inspect()
assert(present and block.name == "minecraft:stone", "Expected pulled stone in front of turtle")
