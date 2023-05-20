package site.siredvin.turtlematic.computercraft.peripheral.misc

import dan200.computercraft.api.peripheral.IComputerAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import site.siredvin.peripheralium.computercraft.peripheral.OwnedPeripheral
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.turtlematic.api.PeripheralConfiguration
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.util.ChunkManager
import java.util.*


class ChunkVialPeripheral(peripheralOwner: TurtlePeripheralOwner) :
    OwnedPeripheral<TurtlePeripheralOwner>(TYPE, peripheralOwner) {
    companion object: PeripheralConfiguration {
        override val TYPE = "chunk_vial"
        private const val UUID_TAG = "uuid"
    }

    private var loadedChunk: ChunkPos? = null
    override val isEnabled: Boolean
        get() = TurtlematicConfig.enableChunkVial

    private val uuid: UUID
        get() {
            val storage = peripheralOwner.dataStorage
            if (!storage.contains(UUID_TAG)) {
                storage.putUUID(UUID_TAG, UUID.randomUUID());
                peripheralOwner.markDataStorageDirty();
            }
            return storage.getUUID(UUID_TAG)
        }

    private val chunkPos: ChunkPos
        get() = peripheralOwner.level!!.getChunk(peripheralOwner.pos).pos

    fun updateChunkState() {
        val level = level as ServerLevel
        val manager = ChunkManager.get(level)
        if (loadedChunk == null || loadedChunk!! != chunkPos) {
            setLoadedChunk(chunkPos, manager, level)
        } else {
            manager.touch(uuid)
        }
    }

    private fun setLoadedChunk(newChunk: ChunkPos?, manager: ChunkManager, level: ServerLevel) {
        if (loadedChunk != null) {
            manager.removeForceChunk(level, uuid)
            loadedChunk = null
        }
        if (newChunk != null) {
            loadedChunk = newChunk
            manager.addForceChunk(level, uuid, loadedChunk!!)
        }
    }

    override fun detach(computer: IComputerAccess) {
        super.detach(computer)
        val level = peripheralOwner.level as ServerLevel
        val manager = ChunkManager.get(level)
        setLoadedChunk(null, manager, level)
    }
}
