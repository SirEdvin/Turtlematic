local automata = assert(peripheral.find("mercantileAutomata"), "Mercantile automata is missing")

local merchants = automata.scan("entity", 4)
assert(#merchants == 1 and merchants[1].name == "minecraft:villager", "Expected villager scan")
local merchant = automata.look("entity")
assert(merchant and merchant.name == "minecraft:villager", "Expected villager inspection")

local count, err = automata.trade(1, "up")
assert(count == nil and err == "No merchant found", "Expected missing merchant error")
count, err = automata.trade()
assert(count == nil and err == "No matching trades found", "Expected out-of-stock trade error")
assert(automata.restock(), "Expected merchant restock to succeed")

turtle.select(1)
count, err = automata.trade()
assert(count == 3, "Expected trade to return three bread: " .. tostring(err))
assert(turtle.getItemCount(1) == 2, "Expected two emeralds to be consumed")
local bread = turtle.getItemDetail(2)
assert(bread and bread.name == "minecraft:bread" and bread.count == 3, "Expected bread from trade")

assert(turtle.drop(1), "Expected one emerald to be removed")
count, err = automata.trade()
assert(count == nil and err == "Not enough items to complete trade", "Expected insufficient payment error")
