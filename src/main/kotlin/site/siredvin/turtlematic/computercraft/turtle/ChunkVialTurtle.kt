package site.siredvin.turtlematic.computercraft.turtle

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.common.items.TurtleItem
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.computercraft.turtle.PeripheralTurtleUpgrade
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.peripheral.misc.ChunkVialPeripheral

class ChunkVialTurtle(item: TurtleItem): PeripheralTurtleUpgrade<ChunkVialPeripheral>(item.turtleID, item.defaultInstance){
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