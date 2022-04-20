package site.siredvin.lib.integrations

import net.minecraft.core.BlockPos
import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

interface IPeripheralIntegration {
    fun isSuitable(level: Level, blockPos: BlockPos, direction: Direction): Boolean
    fun buildPeripheral(level: Level, blockPos: BlockPos, direction: Direction): IPeripheral

    /**
     * @return integration priority, lower priority is better
     */
    val priority: Int
}