local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

assert(bow.shoot(1, 1, true), "Expected suppressed arrow item shot")
assert(turtle.getItemCount(1) == 0, "Expected suppressed arrow to be consumed")
sleep(0.1)
