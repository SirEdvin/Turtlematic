local automata = assert(peripheral.find("brewingAutomata"), "Brewing automata is missing")

turtle.select(1)
local ok, err = automata.brew()
assert(ok, "Expected brewing to succeed: " .. tostring(err))
assert(turtle.getItemCount(1) == 1, "Expected one ingredient to be consumed")
assert(automata.getStoredXP() > 0, "Expected brewing to award XP")

turtle.select(5)
ok, err = automata.throwPotion(1, 0)
assert(ok, "Expected potion throw to succeed: " .. tostring(err))
assert(turtle.getItemCount(5) == 0, "Expected thrown potion to be consumed")
