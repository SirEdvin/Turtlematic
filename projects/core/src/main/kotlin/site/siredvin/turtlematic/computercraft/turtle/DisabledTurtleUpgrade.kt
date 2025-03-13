package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.turtlematic.TurtlematicCore
import site.siredvin.tweakium.modules.peripheral.DisabledPeripheral
import site.siredvin.tweakium.modules.turtle.PeripheralTurtleUpgrade

class DisabledTurtleUpgrade(id: ResourceLocation, item: ItemStack) : PeripheralTurtleUpgrade<DisabledPeripheral>(id, item) {
    init {
        TurtlematicCore.logger.warn("Creating new disabled turtle upgrade with id $id and for item ${item.item}, something clearly wrong here")
    }
    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): DisabledPeripheral = DisabledPeripheral
}
