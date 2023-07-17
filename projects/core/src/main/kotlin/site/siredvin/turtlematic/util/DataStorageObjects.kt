package site.siredvin.turtlematic.util

import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.world.level.block.state.BlockState
import site.siredvin.peripheralium.api.peripheral.IPeripheralOwner
import site.siredvin.peripheralium.util.DataStorageUtil
import site.siredvin.peripheralium.xplat.XplatRegistries
import site.siredvin.turtlematic.tags.BlockTags
import kotlin.math.max

object DataStorageObjects {

    abstract class AbstractDataObject<T> {
        abstract val nbtTag: String

        abstract fun read(data: CompoundTag): T?
        abstract fun write(data: CompoundTag, value: T): Boolean

        operator fun get(storage: CompoundTag): T? {
            if (!storage.contains(nbtTag)) return null
            val value = read(storage)
            if (value == null) {
                storage.remove(nbtTag)
                return null
            }
            return value
        }
        operator fun get(access: ITurtleAccess, side: TurtleSide): T? {
            return get(DataStorageUtil.getDataStorage(access, side))
        }

        operator fun get(access: IPocketAccess): T? {
            return get(DataStorageUtil.getDataStorage(access))
        }

        operator fun get(owner: IPeripheralOwner): T? {
            return get(owner.dataStorage)
        }

        operator fun set(storage: CompoundTag, value: T?) {
            if (value == null) {
                storage.remove(nbtTag)
            } else {
                val writeResult = write(storage, value)
                if (!writeResult) {
                    storage.remove(nbtTag)
                }
            }
        }
        operator fun set(owner: IPeripheralOwner, blockState: T?) {
            set(owner.dataStorage, blockState)
            owner.markDataStorageDirty()
        }

        operator fun set(access: IPocketAccess, blockState: T?) {
            set(DataStorageUtil.getDataStorage(access), blockState)
            access.updateUpgradeNBTData()
        }

        operator fun set(access: ITurtleAccess, side: TurtleSide, blockState: T?) {
            set(DataStorageUtil.getDataStorage(access, side), blockState)
            access.updateUpgradeNBTData(side)
        }
    }

    abstract class AbstractNotNullDataObject<T> {
        abstract val nbtTag: String

        abstract val default: T

        abstract fun read(data: CompoundTag): T
        abstract fun write(data: CompoundTag, value: T): Boolean

        operator fun get(storage: CompoundTag): T {
            if (!storage.contains(nbtTag)) return default
            val value = read(storage)
            if (value == null) {
                storage.remove(nbtTag)
                return default
            }
            return value
        }
        operator fun get(access: ITurtleAccess, side: TurtleSide): T {
            return get(DataStorageUtil.getDataStorage(access, side))
        }

        operator fun get(access: IPocketAccess): T {
            return get(DataStorageUtil.getDataStorage(access))
        }

        operator fun get(owner: IPeripheralOwner): T {
            return get(owner.dataStorage)
        }

        operator fun set(storage: CompoundTag, value: T) {
            val writeResult = write(storage, value)
            if (!writeResult) {
                storage.remove(nbtTag)
            }
        }
        operator fun set(owner: IPeripheralOwner, blockState: T) {
            set(owner.dataStorage, blockState)
            owner.markDataStorageDirty()
        }

        operator fun set(access: IPocketAccess, blockState: T) {
            set(DataStorageUtil.getDataStorage(access), blockState)
            access.updateUpgradeNBTData()
        }

        operator fun set(access: ITurtleAccess, side: TurtleSide, blockState: T) {
            set(DataStorageUtil.getDataStorage(access, side), blockState)
            access.updateUpgradeNBTData(side)
        }
    }

    /**
     * This class is for persistent data sharing between peripherals and another part of systems
     * Like, for example, for ModelTransformingTurtle logic, because it executed on client side where
     * not peripheral is available!
     */
    object RotationCharge : AbstractNotNullDataObject<Int>() {
        const val ROTATION_STEPS = 36
        const val MAX_ROTATION_CHARGE = 3 * ROTATION_STEPS
        override val default: Int
            get() = 0

        override val nbtTag: String
            get() = "rotationCharge"

        override fun read(data: CompoundTag): Int {
            return data.getInt(nbtTag)
        }

        override fun write(data: CompoundTag, value: Int): Boolean {
            data.putInt(nbtTag, value)
            return true
        }

        fun consume(access: ITurtleAccess, side: TurtleSide): Boolean {
            val currentCharge = this[access, side]
            if (currentCharge > 0) {
                this[access, side] = max(0, currentCharge - 1)
                return true
            }
            return false
        }

        fun addCycles(owner: IPeripheralOwner, count: Int) {
            val currentRotationCharge = this[owner]
            if (currentRotationCharge < MAX_ROTATION_CHARGE) {
                this[owner] = max(0, currentRotationCharge) + count * ROTATION_STEPS
            }
        }
    }

    object Mimic : AbstractDataObject<BlockState>() {
        override val nbtTag: String
            get() = "mimic"

        override fun read(data: CompoundTag): BlockState? {
            val blockState = NbtUtils.readBlockState(XplatRegistries.BLOCKS, data.getCompound(nbtTag))
            if (blockState.`is`(BlockTags.MIMIC_BLOCKLIST)) {
                return null
            }
            return blockState
        }

        override fun write(data: CompoundTag, value: BlockState): Boolean {
            if (value.`is`(BlockTags.MIMIC_BLOCKLIST)) {
                return false
            }
            data.put(nbtTag, NbtUtils.writeBlockState(value))
            return true
        }
    }

    object MimicExtraData : AbstractDataObject<CompoundTag>() {

        override val nbtTag: String
            get() = "mimicNBTData"

        override fun read(data: CompoundTag): CompoundTag? {
            if (!data.contains(nbtTag)) return null
            return data.getCompound(nbtTag)
        }

        override fun write(data: CompoundTag, value: CompoundTag): Boolean {
            data.put(nbtTag, value)
            return true
        }
    }

    object RMLInstructions : AbstractDataObject<String>() {
        override val nbtTag: String
            get() = "rml"

        override fun read(data: CompoundTag): String? {
            if (!data.contains(nbtTag)) return null
            return data.getString(nbtTag)
        }

        override fun write(data: CompoundTag, value: String): Boolean {
            data.putString(nbtTag, value)
            return true
        }
    }

    object Angle : AbstractNotNullDataObject<Double>() {
        override val nbtTag: String
            get() = "angle"

        override val default: Double
            get() = 0.0

        override fun read(data: CompoundTag): Double {
            return data.getDouble(nbtTag)
        }

        override fun write(data: CompoundTag, value: Double): Boolean {
            data.putDouble(nbtTag, value)
            return true
        }
    }

    object TurtleChat : AbstractDataObject<String>() {
        override val nbtTag: String
            get() = "chatMessage"

        override fun read(data: CompoundTag): String? {
            if (data.contains(nbtTag)) {
                return data.getString(nbtTag)
            }
            return null
        }

        override fun write(data: CompoundTag, value: String): Boolean {
            data.putString(nbtTag, value)
            return true
        }
    }
}
