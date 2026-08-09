local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

bow.setAngle(0)
assert(bow.getAngle() == 0)
assert(not pcall(bow.shoot, 0), "Expected non-positive power to fail")
assert(bow.shoot(1, 1), "Expected arrow shot to succeed")
assert(turtle.getItemCount(1) == 1, "Expected one arrow to be consumed")
sleep(0.1)
