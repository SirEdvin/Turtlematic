local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

assert(bow.shoot(1, 1), "Expected spectral arrow shot")
assert(turtle.getItemCount(1) == 0, "Expected spectral arrow to be consumed")
sleep(0.1)
