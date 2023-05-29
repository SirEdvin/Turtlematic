package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.TagParser
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import site.siredvin.peripheralium.api.storage.ContainerUtils
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import java.util.Optional

class CreativeChestPeripheral(turtle: ITurtleAccess, side: TurtleSide) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val TYPE = "creative_chest"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableCreativeChest

    @LuaFunction(mainThread = true)
    fun generate(item: String, amount: Int, nbtData: Optional<String>): MethodResult {
        val targetItem = XplatRegistries.ITEMS.get(ResourceLocation(item))
        if (targetItem == Items.AIR) {
            return MethodResult.of(null, "Item with this ID not found")
        }
        val itemStack = targetItem.defaultInstance
        itemStack.count = amount
        if (nbtData.isPresent) {
            itemStack.tag = TagParser.parseTag(nbtData.get())
        }
        ContainerUtils.storeItem(peripheralOwner.turtle.inventory, itemStack)
        return MethodResult.of(true)
    }
}
