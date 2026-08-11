local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

bow.setAngle(0)
assert(bow.shoot(2, 1), "Expected item shot to succeed")
assert(turtle.getItemCount(1) == 2, "Expected one item to be fired")
sleep(0.5)

assert(turtle.forward() and turtle.forward(), "Failed to approach target chest")
turtle.select(2)
assert(turtle.suck(), "Expected to retrieve fired item from chest")
assert(turtle.getItemCount(2) == 1, "Expected fired item in target chest")
