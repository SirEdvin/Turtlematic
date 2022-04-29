package site.siredvin.lib.peripherals.owner

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import site.siredvin.lib.blocks.LibTileEntityBlock
import site.siredvin.lib.peripherals.api.IPeripheralTileEntity
import site.siredvin.lib.util.DataStorageUtil
import site.siredvin.lib.util.LibFakePlayer
import java.util.*

class BlockEntityPeripheralOwner<T>(val tileEntity: T) :
    BasePeripheralOwner() where T : BlockEntity, T : IPeripheralTileEntity {

    override val level: Level?
        get() = Objects.requireNonNull(tileEntity.level)
    override val pos: BlockPos
        get() = tileEntity.blockPos

    override val facing: Direction
        get() = tileEntity.blockState.getValue(LibTileEntityBlock.FACING);

    override val owner: Player?
        get() = null
    override val dataStorage: CompoundTag
        get() = DataStorageUtil.getDataStorage(tileEntity)

    override fun markDataStorageDirty() {
        tileEntity.setChanged()
    }

    override fun <T> withPlayer(function: (LibFakePlayer) -> T, overwrittenDirection: Direction?): T {
        throw RuntimeException("Not implemented yet")
    }

    override val toolInMainHand: ItemStack
        get() = ItemStack.EMPTY

    override fun storeItem(stored: ItemStack): ItemStack {
        throw RuntimeException("Not implemented yet")
    }

    override fun destroyUpgrade() {
        level!!.removeBlock(tileEntity.blockPos, false)
    }

    override fun isMovementPossible(level: Level, pos: BlockPos): Boolean {
        return false
    }

    override fun move(level: Level, pos: BlockPos): Boolean {
        return false
    }
}