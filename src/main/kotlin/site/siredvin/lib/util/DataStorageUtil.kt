package site.siredvin.lib.util

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import net.minecraft.nbt.CompoundTag
import site.siredvin.lib.peripherals.IPeripheralTileEntity
import dan200.computercraft.api.pocket.IPocketAccess
import site.siredvin.lib.peripherals.owner.IPeripheralOwner
import kotlin.math.max

object DataStorageUtil {
    fun getDataStorage(access: ITurtleAccess, side: TurtleSide?): CompoundTag {
        return access.getUpgradeNBTData(side)
    }

    fun getDataStorage(tileEntity: IPeripheralTileEntity): CompoundTag {
        return tileEntity.peripheralSettings
    }

    fun getDataStorage(pocket: IPocketAccess): CompoundTag {
        return pocket.upgradeNBTData
    }

    /**
     * This class is for persistent data sharing between peripherals and another part of systems
     * Like, for example, for ModelTransformingTurtle logic, because it executed on client side where
     * not peripheral is available!
     */
    object RotationCharge {
        const val ROTATION_STEPS = 36

        /**
         * Used for gear rotation animation
         */
        private const val ROTATION_CHARGE_SETTING = "rotationCharge"
        operator fun get(access: ITurtleAccess, side: TurtleSide): Int {
            return getDataStorage(access, side).getInt(ROTATION_CHARGE_SETTING)
        }

        fun consume(access: ITurtleAccess, side: TurtleSide): Boolean {
            val data = getDataStorage(access, side)
            val currentCharge = data.getInt(ROTATION_CHARGE_SETTING)
            if (currentCharge > 0) {
                data.putInt(ROTATION_CHARGE_SETTING, max(0, data.getInt(ROTATION_CHARGE_SETTING) - 1))
                access.updateUpgradeNBTData(side)
                return true
            }
            return false
        }

        fun addCycles(owner: IPeripheralOwner, count: Int) {
            val data = owner.dataStorage
            data.putInt(
                ROTATION_CHARGE_SETTING,
                max(0, data.getInt(ROTATION_CHARGE_SETTING)) + count * ROTATION_STEPS
            )
            owner.markDataStorageDirty()
        }
    }
}