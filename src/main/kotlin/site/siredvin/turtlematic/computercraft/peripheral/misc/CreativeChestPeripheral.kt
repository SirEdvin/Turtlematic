package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.shared.util.InventoryUtil
import net.minecraft.core.Registry
import net.minecraft.nbt.TagParser
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import site.siredvin.lib.computercraft.peripheral.BasePeripheral
import site.siredvin.lib.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import java.util.Optional

class CreativeChestPeripheral(peripheralOwner: TurtlePeripheralOwner) :
    BasePeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {

    companion object {
        const val TYPE = "creative_chest"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableCreativeChest

    @LuaFunction(mainThread = true)
    fun generate(item: String, amount: Int, nbtData: Optional<String>): MethodResult {
        val targetItem = Registry.ITEM.get(ResourceLocation(item))
        if (targetItem == Items.AIR)
            return MethodResult.of(null, "Item with this ID not found")
        val itemStack = targetItem.defaultInstance
        itemStack.count = amount
        if (nbtData.isPresent)
            itemStack.tag = TagParser.parseTag(nbtData.get())
        InventoryUtil.storeItems(itemStack, peripheralOwner.turtle.itemHandler)
        return MethodResult.of(true)
    }
}