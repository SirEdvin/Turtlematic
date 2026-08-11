local automata, blockAutomata = peripheral.find("smithingAutomata")
assert(automata and blockAutomata, "Two smithing automata are required")
assert(automata.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")
assert(blockAutomata.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")

local function count(name)
    local total = 0
    for slot = 1, 16 do
        local item = turtle.getItemDetail(slot)
        if item and item.name == name then total = total + item.count end
    end
    return total
end

turtle.select(1)
local ok, err = automata.smelt("inventory", 2)
assert(ok, "Expected inventory smelting to succeed: " .. tostring(err))
assert(count("minecraft:raw_iron") == 1 and count("minecraft:iron_ingot") == 2, "Expected limited inventory smelting")

ok, err = blockAutomata.smelt("block")
assert(ok, "Expected block smelting to succeed: " .. tostring(err))
assert(automata.look("block").name == "minecraft:stone", "Expected cobblestone to smelt in place")

turtle.select(5)
ok, err = automata.smith()
assert(ok, "Expected smithing to succeed: " .. tostring(err))
assert(count("minecraft:netherite_sword") == 1, "Expected a netherite sword")
