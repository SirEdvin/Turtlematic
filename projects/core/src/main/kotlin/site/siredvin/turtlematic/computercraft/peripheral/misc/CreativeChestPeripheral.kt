package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.TagParser
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import site.siredvin.broccolium.modules.platform.PlatformRegistries
import site.siredvin.broccolium.modules.storage.item.ContainerUtils
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.tweakium.modules.peripheral.OwnedPeripheral
import site.siredvin.tweakium.modules.peripheral.owner.TurtlePeripheralOwner
import java.util.Optional

class CreativeChestPeripheral(turtle: ITurtleAccess, side: TurtleSide) : OwnedPeripheral<TurtlePeripheralOwner>(type, TurtlePeripheralOwner(turtle, side)) {

    companion object : PeripheralConfiguration {
        override val type = "creative_chest"
    }

    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableCreativeChest

    @LuaFunction(mainThread = true)
    fun generate(item: String, amount: Int, nbtData: Optional<String>): MethodResult {
        val targetItem = PlatformRegistries.ITEMS.get(ResourceLocation(item))
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
