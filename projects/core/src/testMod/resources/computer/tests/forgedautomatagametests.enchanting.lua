local automata, extractor = peripheral.find("enchantingAutomata")
assert(automata and extractor, "Two enchanting automata are required")
assert(automata.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")
assert(extractor.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")

assert(automata.collectXP() == 1000, "Expected nearby XP to be collected")
turtle.select(1)
local choices, err = automata.getPossibleEnchantments()
assert(choices and #choices > 0, "Expected enchantment choices: " .. tostring(err))

local ok
ok, err = automata.enchant(1)
assert(ok, "Expected enchanting to succeed: " .. tostring(err))

ok, err = extractor.extractEnchantment(2)
assert(ok, "Expected enchantment extraction to succeed: " .. tostring(err))
assert(turtle.getItemDetail(2).name == "minecraft:enchanted_book", "Expected an enchanted book")
