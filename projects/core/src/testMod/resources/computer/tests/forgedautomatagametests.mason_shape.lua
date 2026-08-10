local automata = assert(peripheral.find("masonAutomata"), "Mason automata is missing")

local shapes = automata.getPossibleShapes()
assert(shapes and #shapes > 0, "Expected alternative stair shapes")
local ok, err = automata.changeShape(shapes[1])
assert(ok, "Expected stair shape change to succeed: " .. tostring(err))
