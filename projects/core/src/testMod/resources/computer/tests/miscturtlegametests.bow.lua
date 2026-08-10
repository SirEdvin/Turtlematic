local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

bow.setAngle(0)
assert(bow.getAngle() == 0)
assert(not pcall(bow.shoot, 0), "Expected non-positive power to fail")
assert(not pcall(bow.shoot, 1, 0), "Expected non-positive limit to fail")

turtle.select(2)
local ok, err = bow.shoot(1)
assert(ok == nil and err == "Nothing to shoot", "Expected empty-slot error")

turtle.select(1)
assert(bow.shoot(1, 1), "Expected ordinary arrow shot")
sleep(0.1)
