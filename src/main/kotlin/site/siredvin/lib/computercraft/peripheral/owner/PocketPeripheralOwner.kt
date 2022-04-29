package site.siredvin.lib.computercraft.peripheral.owner

import dan200.computercraft.api.pocket.IPocketAccess
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.util.DataStorageUtil
import site.siredvin.lib.util.world.LibFakePlayer

class PocketPeripheralOwner(private val pocket: IPocketAccess?) : BasePeripheralOwner() {
    override val level: Level?
        get() {
            val owner = pocket?.entity ?: return null
            return owner.commandSenderWorld
        }
    override val pos: BlockPos
        get() {
            val owner = pocket?.entity ?: return BlockPos(0, 0, 0)
            return owner.blockPosition()
        }
    override val facing: Direction
        get() {
            val owner = pocket?.entity ?: return Direction.NORTH
            return owner.direction
        }
    override val owner: Player?
        get() {
            val owner = pocket?.entity
            return if (owner is Player) owner else null
        }
    override val dataStorage: CompoundTag
        get() = pocket?.let { DataStorageUtil.getDataStorage(it) } ?: CompoundTag()

    override fun markDataStorageDirty() {
        pocket?.updateUpgradeNBTData()
    }

    override fun <T> withPlayer(function: (LibFakePlayer) -> T, overwrittenDirection: Direction?): T {
        throw RuntimeException("Not implemented yet")
    }

    override val toolInMainHand: ItemStack
        get() = ItemStack.EMPTY

    override fun storeItem(stored: ItemStack): ItemStack {
        // Tricks with inventory needed
        throw RuntimeException("Not implemented yet")
    }

    override fun destroyUpgrade() {
        throw RuntimeException("Not implemented yet")
    }

    override fun isMovementPossible(level: Level, pos: BlockPos): Boolean {
        return false
    }

    override fun move(level: Level, pos: BlockPos): Boolean {
        return false
    }
}