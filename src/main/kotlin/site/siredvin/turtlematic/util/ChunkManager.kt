package site.siredvin.turtlematic.util

import kotlinx.datetime.Clock
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData
import site.siredvin.turtlematic.Turtlematic
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import java.util.*


class ChunkManager: SavedData() {
    companion object {
        private const val DIMENSION_NAME_TAG = "dimensionName"
        private const val POS_TAG = "pos"
        private const val DATA_NAME = Turtlematic.MOD_ID + "_ForcedChunks"
        private const val FORCED_CHUNKS_TAG = "forcedChunks"

        private fun readChunkRecord(tag: CompoundTag): LoadChunkRecord {
            return LoadChunkRecord(
                tag.getString(DIMENSION_NAME_TAG), chunkPosFromNBT(tag.getCompound(POS_TAG))
            )
        }

        fun load(data: CompoundTag): ChunkManager {
            val manager = ChunkManager()
            val forcedData = data.getCompound(FORCED_CHUNKS_TAG)
            for (key in forcedData.allKeys) {
                manager.forcedChunks[UUID.fromString(key)] = readChunkRecord(forcedData.getCompound(key))
            }
            Turtlematic.LOGGER.info("Loaded ${manager.forcedChunks.size} forced chunks")
            return manager
        }

        fun get(level: ServerLevel): ChunkManager {
            return level.dataStorage.computeIfAbsent(ChunkManager::load, { ChunkManager() }, DATA_NAME)
        }

        fun registerHooks() {
            ServerLifecycleEvents.SERVER_STARTED.register(ServerLifecycleEvents.ServerStarted {
                get(it.overworld()).init(it)
            })
            ServerLifecycleEvents.SERVER_STOPPING.register(ServerLifecycleEvents.ServerStopping {
                get(it.overworld()).stop(it)
            })
            ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick {
                get(it.overworld()).tick(it)
            })
        }

    }

    private var tickCounter = 0L
    private val forcedChunks: MutableMap<UUID, LoadChunkRecord> = mutableMapOf()
    private var initialized = false
    private var mainThread: Thread? = null

    @Synchronized
    fun addForceChunk(level: ServerLevel, owner: UUID, pos: ChunkPos): Boolean {
        if (forcedChunks.containsKey(owner)) {
            Turtlematic.LOGGER.debug("Chunk re-added to force loaded {} with touch", pos)
            forcedChunks[owner]?.touch()
            return true
        }
        Turtlematic.LOGGER.debug("Chunk added to force loaded {}", pos)
        forcedChunks[owner] = LoadChunkRecord(level.dimension().location().toString(), pos)
        setDirty()
        return level.setChunkForced(pos.x, pos.z, false)
    }

    @Synchronized
    fun touch(owner: UUID) {
        forcedChunks[owner]?.touch()
    }

    /**
     * So, we need this thread check, because peripheral can be detached also in computer thread
     * and removing chunk from loading in computer thread lead to expected crash
     */
    @Synchronized
    fun   removeChunk(owner: UUID, pos: ChunkPos, level: ServerLevel): Boolean {
        if (mainThread != null && Thread.currentThread() == mainThread) {
            Turtlematic.LOGGER.debug("Chunk removed from to force loaded {}", pos)
            return level.setChunkForced(pos.x, pos.z, true)
        }
        Turtlematic.LOGGER.debug("Market chunk to remove {}", pos)
        val forcedChunk = forcedChunks[owner] ?: return false
        forcedChunk.invalidate()
        return false
    }

    @Synchronized
    fun removeForceChunk(level: ServerLevel, owner: UUID, loadedChunk: ChunkPos? = null): Boolean {
        if (!forcedChunks.containsKey(owner)) return true
        val chunkRecord = forcedChunks[owner] ?: return true
        val dimensionName = level.dimension().location().toString()
        require(
            chunkRecord.dimensionName == dimensionName
        ) {
            java.lang.String.format(
                "Incorrect dimension! Should be %s instead of %s",
                chunkRecord.dimensionName,
                dimensionName
            )
        }
        val result: Boolean = if (loadedChunk == null) {
            removeChunk(owner, chunkRecord.pos, level)
        } else {
            removeChunk(owner, loadedChunk, level)
        }
        if (result) {
            forcedChunks.remove(owner)
            setDirty()
        }
        return result
    }

    @Synchronized
    fun init(server: MinecraftServer) {
        if (!initialized) {
            mainThread = server.runningThread
            server.allLevels.forEach { level ->
                val dimensionName: String = level.dimension().location().toString()
                forcedChunks.forEach {
                    if (it.value.dimensionName == dimensionName) {
                        level.setChunkForced(it.value.pos.x, it.value.pos.z, false)
                    }
                }
            }
            initialized = true
        }
    }

    @Synchronized
    fun stop(server: MinecraftServer) {
        if (initialized) {
            server.allLevels.forEach { level ->
                val dimensionName: String = level.dimension().location().toString()
                forcedChunks.entries.forEach {
                    if (it.value.dimensionName == dimensionName) {
                        level.setChunkForced(it.value.pos.x, it.value.pos.z, true)
                    }
                }
            }
            initialized = false
        }
    }

    @Synchronized
    fun tick(server: MinecraftServer) {
        tickCounter++
        // Performs cleanup every second
        if (tickCounter % 20 == 0L) {
            server.allLevels.forEach { level ->
                val dimensionName: String = level.dimension().location().toString()
                val purgeList: List<UUID> = forcedChunks.entries.mapNotNull {
                    if (it.value.dimensionName == dimensionName && !it.value.valid)
                        return@mapNotNull it.key
                    return@mapNotNull null
                }
                purgeList.forEach {
                    removeForceChunk(level, it)
                }
            }
        }
    }

    override fun save(compoundTag: CompoundTag): CompoundTag {
        Turtlematic.LOGGER.info("Saving all forces chunks ${forcedChunks.entries.size}")
        val forcedChunksTag = CompoundTag()
        forcedChunks.forEach { (key, value) ->
            forcedChunksTag.put(
                key.toString(),
                value.serialize()
            )
        }
        compoundTag.put(FORCED_CHUNKS_TAG, forcedChunksTag)
        return compoundTag
    }

    internal data class LoadChunkRecord(
        val dimensionName: String,
        val pos: ChunkPos,
        var lastTouch: Long = Clock.System.now().toEpochMilliseconds()
    ) {
        val valid: Boolean
            get() {
                val currentEpoch: Long = Clock.System.now().toEpochMilliseconds()
                return lastTouch + TurtlematicConfig.chunkLoadedTimeLimit >= currentEpoch
            }

        fun touch() {
            lastTouch = Clock.System.now().toEpochMilliseconds()
        }

        fun invalidate() {
            lastTouch = Clock.System.now().toEpochMilliseconds() - TurtlematicConfig.chunkLoadedTimeLimit / 2
        }

        fun serialize(): CompoundTag {
            val tag = CompoundTag()
            tag.putString(DIMENSION_NAME_TAG, dimensionName)
            tag.put(POS_TAG, toNBT(pos))
            return tag
        }
    }
}