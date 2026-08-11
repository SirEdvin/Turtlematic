local automata, blockAutomata = peripheral.find("masonAutomata")
assert(automata and blockAutomata, "Two mason automata are required")
assert(automata.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")
assert(blockAutomata.setFuelConsumptionRate(4), "Expected maximum fuel consumption rate")

local function contains(values, expected)
    for _, value in ipairs(values) do
        if value == expected then return true end
    end
    return false
end

turtle.select(1)
assert(contains(automata.getAlternatives("inventory"), "minecraft:stone_bricks"), "Expected stone brick alternative")
local ok, err = automata.chisel("inventory", "minecraft:stone_bricks", 2)
assert(ok, "Expected inventory chiseling to succeed: " .. tostring(err))

ok, err = blockAutomata.chisel("block", "minecraft:stone_bricks", "up")
assert(ok, "Expected block chiseling to succeed: " .. tostring(err))
assert(blockAutomata.look("block", "up").name == "minecraft:stone_bricks", "Expected block to be chiseled in place")

ok, err = blockAutomata.rotate("clockwise_90")
assert(ok, "Expected stair rotation to succeed: " .. tostring(err))
ok, err = automata.turnOver()
assert(ok, "Expected stair turnover to succeed: " .. tostring(err))
