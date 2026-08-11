local scrapper = assert(peripheral.find("soul_scrapper"), "Soul scrapper upgrade is missing")

turtle.select(2)
local ok, err = scrapper.harvestSoul()
assert(ok == nil and err ~= nil, "Expected non-feedable item to fail")

turtle.select(1)
ok, err = scrapper.harvestSoul()
assert(ok, "Expected pig soul harvest to succeed: " .. tostring(err))
local remaining = scrapper.getLeftEntities()
assert(#remaining == 3, "Expected three husbandry souls to remain")
for _, entity in ipairs(remaining) do assert(entity.leftCount == 1) end
