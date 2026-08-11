local chatter = assert(peripheral.find("chatter"), "Chatter upgrade is missing")

assert(chatter.getMessage() == nil)
chatter.setMessage("hello turtle")
assert(chatter.getMessage() == "hello turtle")
chatter.clearMessage()
assert(chatter.getMessage() == nil)

assert(chatter.getColor() == nil)
chatter.setColor(0x336699)
assert(chatter.getColor() == 0x336699)
chatter.clearColor()
assert(chatter.getColor() == nil)
