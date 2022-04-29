package site.siredvin.lib.peripherals.api

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

interface IPeripheralTileEntity {
    val peripheralSettings: CompoundTag
    fun markSettingsChanged()
    fun <T : BlockEntity?> handleTick(level: Level?, state: BlockState?, type: BlockEntityType<T>?) {}
}