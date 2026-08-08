assert(peripheral.find("chunk_vial"), "Chunk vial upgrade is missing")
sleep(0.1)
assert(turtle.equipLeft(), "Failed to detach chunk vial")
assert(peripheral.find("chunk_vial") == nil, "Chunk vial remained attached")
