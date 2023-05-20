package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.peripheral.misc.ChunkVialPeripheral

class ChunkVialTurtle(id: ResourceLocation, itemStack: ItemStack): PeripheralTurtleUpgrade<ChunkVialPeripheral>(id, itemStack){
    override fun buildPeripheral(turtle: ITurtleAccess, side: TurtleSide): ChunkVialPeripheral {
        return ChunkVialPeripheral(TurtlePeripheralOwner(turtle, side))
    }

    override fun update(turtle: ITurtleAccess, side: TurtleSide) {
        super.update(turtle, side)
        if (TurtlematicConfig.enableChunkVial) {
            val peripheral = turtle.getPeripheral(side) as? ChunkVialPeripheral ?: return
            peripheral.updateChunkState()
        }
    }
}