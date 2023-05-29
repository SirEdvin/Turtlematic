package site.siredvin.turtlematic.util

import dan200.computercraft.api.turtle.ITurtleAccess
import dan200.computercraft.api.turtle.TurtleSide
import site.siredvin.peripheralium.api.peripheral.IPeripheralOwner
import site.siredvin.peripheralium.util.DataStorageUtil
import kotlin.math.max

object DataStorageObjects {

    /**
     * This class is for persistent data sharing between peripherals and another part of systems
     * Like, for example, for ModelTransformingTurtle logic, because it executed on client side where
     * not peripheral is available!
     */
    object RotationCharge {
        const val ROTATION_STEPS = 36
        const val MAX_ROTATION_CHARGE = 3 * ROTATION_STEPS

        /**
         * Used for gear rotation animation
         */
        private const val ROTATION_CHARGE_SETTING = "rotationCharge"
        operator fun get(access: ITurtleAccess, side: TurtleSide): Int {
            return DataStorageUtil.getDataStorage(access, side).getInt(ROTATION_CHARGE_SETTING)
        }

        fun consume(access: ITurtleAccess, side: TurtleSide): Boolean {
            val data = DataStorageUtil.getDataStorage(access, side)
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
            val currentRotationCharge = data.getInt(ROTATION_CHARGE_SETTING)
            if (currentRotationCharge < MAX_ROTATION_CHARGE) {
                data.putInt(
                    ROTATION_CHARGE_SETTING,
                    max(0, currentRotationCharge) + count * ROTATION_STEPS,
                )
                owner.markDataStorageDirty()
            }
        }
    }

    object Angle {

        private const val ANGLE_SETTINGS = "angle"
        operator fun get(access: ITurtleAccess, side: TurtleSide): Double {
            return DataStorageUtil.getDataStorage(access, side).getDouble(ANGLE_SETTINGS)
        }

        operator fun get(owner: IPeripheralOwner): Double {
            return owner.dataStorage.getDouble(ANGLE_SETTINGS)
        }

        operator fun set(owner: IPeripheralOwner, value: Double) {
            owner.dataStorage.putDouble(ANGLE_SETTINGS, value)
        }
    }

    object TurtleChat {
        private const val CHAT_MESSAGE = "chatMessage"

        fun getMessage(access: ITurtleAccess, side: TurtleSide): String? {
            val data = DataStorageUtil.getDataStorage(access, side)
            if (data.contains(CHAT_MESSAGE)) {
                return data.getString(CHAT_MESSAGE)
            }
            return null
        }

        fun getMessage(owner: IPeripheralOwner): String? {
            val data = owner.dataStorage
            if (data.contains(CHAT_MESSAGE)) {
                return data.getString(CHAT_MESSAGE)
            }
            return null
        }

        fun setMessage(owner: IPeripheralOwner, message: String?) {
            val data = owner.dataStorage
            if (message == null) {
                data.remove(CHAT_MESSAGE)
            } else {
                data.putString(CHAT_MESSAGE, message)
            }
            owner.markDataStorageDirty()
        }
    }
}
