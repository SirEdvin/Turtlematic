local bow = assert(peripheral.find("bow"), "Bow upgrade is missing")

assert(bow.shoot(1, 1), "Expected tipped arrow shot")
assert(turtle.getItemCount(1) == 0, "Expected tipped arrow to be consumed")
sleep(0.1)
