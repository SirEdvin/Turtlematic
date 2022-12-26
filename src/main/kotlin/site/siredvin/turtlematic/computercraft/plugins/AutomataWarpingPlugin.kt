package site.siredvin.turtlematic.computercraft.plugins

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import site.siredvin.turtlematic.computercraft.operations.SingleOperationContext
import site.siredvin.turtlematic.computercraft.peripheral.automatas.BaseAutomataCorePeripheral
import site.siredvin.peripheralium.api.peripheral.IPeripheralCheck
import site.siredvin.peripheralium.api.peripheral.IPeripheralFunction
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.peripheralium.computercraft.peripheral.ability.FuelAbility
import site.siredvin.peripheralium.computercraft.peripheral.ability.PeripheralOwnerAbility
import site.siredvin.peripheralium.computercraft.peripheral.owner.TurtlePeripheralOwner
import site.siredvin.peripheralium.util.NBTUtil.blockPosFromNBT
import site.siredvin.peripheralium.util.NBTUtil.toNBT
import site.siredvin.turtlematic.common.configuration.TurtlematicConfig
import site.siredvin.turtlematic.computercraft.operations.SingleOperation

class AutomataWarpingPlugin(automataCore: BaseAutomataCorePeripheral) : AutomataCorePlugin(automataCore) {
    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(SingleOperation.WARP)

    protected val pointData: CompoundTag
        get() {
            val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
            val settings: CompoundTag = owner.dataStorage
            if (!settings.contains(WORLD_DATA_MARK)) {
                settings.putString(WORLD_DATA_MARK, owner.level!!.dimension().location().toString())
            } else {
                val worldName: String = settings.getString(WORLD_DATA_MARK)
                if (owner.level!!.dimension().location().toString() != worldName) {
                    throw LuaException("Incorrect world for this upgrade")
                }
            }
            if (!settings.contains(POINT_DATA_MARK)) {
                settings.put(POINT_DATA_MARK, CompoundTag())
            }
            return settings.getCompound(POINT_DATA_MARK)
        }

    private fun getWarpCost(context: SingleOperationContext): Int {
        val fuelAbility: FuelAbility<*> = automataCore.peripheralOwner.getAbility(PeripheralOwnerAbility.FUEL)!!
        return SingleOperation.WARP.getCost(context) * fuelAbility.fuelConsumptionMultiply
    }

    @LuaFunction(mainThread = true)
    fun savePoint(name: String): MethodResult {
        automataCore.addRotationCycle()
        val data: CompoundTag = pointData
        if (data.allKeys.size >= TurtlematicConfig.endAutomataCoreWarpPointLimit) return MethodResult.of(
            null,
            "Cannot add new point, limit reached"
        )
        data.put(name, toNBT(automataCore.peripheralOwner.pos))
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun deletePoint(name: String): MethodResult {
        automataCore.addRotationCycle()
        val data: CompoundTag = pointData
        if (!data.contains(name)) return MethodResult.of(null, "Cannot find point to delete")
        data.remove(name)
        return MethodResult.of(true)
    }

    @LuaFunction(mainThread = true)
    fun points(): MethodResult {
        val data: CompoundTag = pointData
        return MethodResult.of(data.allKeys)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun warpToPoint(name: String): MethodResult {
        val owner: TurtlePeripheralOwner = automataCore.peripheralOwner
        val level: Level = owner.level!!
        val data: CompoundTag = pointData
        if (!data.contains(name))
            return MethodResult.of(null, "Cannot find point to warp to")
        val newPosition: BlockPos = blockPosFromNBT(data.getCompound(name))
        return automataCore.withOperation(
            SingleOperation.WARP,
            automataCore.toDistance(newPosition),
            IPeripheralFunction {
                val result: Boolean = owner.move(level, newPosition)
                if (!result) return@IPeripheralFunction MethodResult.of(null, "Cannot teleport to location")
                MethodResult.of(true)
            },
            IPeripheralCheck {
                if (!owner.isMovementPossible(level, newPosition)) return@IPeripheralCheck MethodResult.of(
                    null,
                    "Move forbidden"
                )
                null
            })
    }

    @LuaFunction(mainThread = true)
    fun estimateWarpCost(name: String): MethodResult {
        val data: CompoundTag = pointData
        val newPosition: BlockPos = blockPosFromNBT(data.getCompound(name))
        return MethodResult.of(getWarpCost(automataCore.toDistance(newPosition)))
    }

    @LuaFunction(mainThread = true)
    fun distanceToPoint(name: String): MethodResult {
        val data: CompoundTag = pointData
        val newPosition: BlockPos = blockPosFromNBT(data.getCompound(name))
        return MethodResult.of(newPosition.distManhattan(automataCore.peripheralOwner.pos))
    }

    companion object {
        private const val POINT_DATA_MARK = "warp_points"
        private const val WORLD_DATA_MARK = "warp_world"
    }
}