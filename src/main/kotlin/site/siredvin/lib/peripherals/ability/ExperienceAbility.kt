package site.siredvin.lib.peripherals.ability

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import site.siredvin.lib.LibConfig
import site.siredvin.lib.peripherals.api.*
import site.siredvin.lib.peripherals.operation.UnconditionalOperations
import site.siredvin.lib.util.LuaConverter
import site.siredvin.lib.util.radiusCorrect
import kotlin.math.min

class ExperienceAbility(val owner: IPeripheralOwner, private val interactionRadius: Int): IOwnerAbility, IPeripheralPlugin {
    companion object {
        private const val COLLECTED_XP_AMOUNT = "CollectedXPAmount"
    }

    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(UnconditionalOperations.XP_TRANSFER) // TODO: Add XP Transfer

    override fun collectConfiguration(data: MutableMap<String?, Any?>) {
        data["xpToFuelRate"] = LibConfig.xpToFuelRate
    }

    protected fun _getStoredXP(): Double {
        return owner.dataStorage.getDouble(COLLECTED_XP_AMOUNT)
    }

    fun adjustStoredXP(amount: Double) {
        owner.dataStorage.putDouble(COLLECTED_XP_AMOUNT, owner.dataStorage.getDouble(COLLECTED_XP_AMOUNT) + amount)
        owner.markDataStorageDirty()
    }

    @Throws(LuaException::class)
    protected fun withXPTransfer(
        function: IPeripheralFunction<Any?, MethodResult>
    ): MethodResult {
        val ability: OperationAbility = owner.getAbility(PeripheralOwnerAbility.OPERATION)!!
        return ability.performOperation(UnconditionalOperations.XP_TRANSFER, null, null, function, null, null)
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun collectXP(): MethodResult {
        return withXPTransfer {
            val level: Level = owner.level!!
            val pos = owner.pos
            val searchBox = AABB(pos).inflate(interactionRadius.toDouble())
            val oldCount = _getStoredXP()
            level.getEntitiesOfClass(ExperienceOrb::class.java, searchBox).forEach { entity ->
                adjustStoredXP(entity.value.toDouble())
                entity.remove(Entity.RemovalReason.KILLED)
            }
            MethodResult.of(_getStoredXP() - oldCount)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun suckOwnerXP(limit: Int): MethodResult {
        return withXPTransfer {
            val player: Player = owner.owner
                ?: return@withXPTransfer MethodResult.of(null, "Cannot find owning player")
            val suckedCount = min(player.totalExperience, limit)
            player.giveExperiencePoints(-suckedCount)
            adjustStoredXP(suckedCount.toDouble())
            MethodResult.of(suckedCount)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun burnXP(limit: Double): Double {
        if (limit <= 0) throw LuaException("Incorrect limit")
        val fuelAbility: FuelAbility<*> = owner.getAbility(PeripheralOwnerAbility.FUEL)
            ?: throw LuaException("Unsupported operation")
        val burnAmount = min(limit, _getStoredXP())
        adjustStoredXP(-burnAmount)
        fuelAbility.addFuel((burnAmount * LibConfig.xpToFuelRate).toInt())
        return burnAmount
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun sendXPToOwner(limit: Int): MethodResult {
        return withXPTransfer {
            val count = min(limit.toDouble(), _getStoredXP())
            val player: Player = owner.owner
                ?: return@withXPTransfer MethodResult.of(null, "Cannot find owning player")
            player.giveExperiencePoints(count.toInt())
            adjustStoredXP(-count)
            MethodResult.of(count)
        }
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun sendXP(rawBlockPos: Map<*, *>, limit: Double): MethodResult {
        val pos: BlockPos = owner.pos
        val targetPos: BlockPos = LuaConverter.convertToBlockPos(pos, rawBlockPos)
        return withXPTransfer {
            if (!radiusCorrect(pos, targetPos, interactionRadius))
                return@withXPTransfer MethodResult.of(null, "Turtle are too far away")

            val abilityExtractResult = AbilityToolkit.extractAbility(PeripheralOwnerAbility.EXPERIENCE, owner.level!!, targetPos)
            if (abilityExtractResult.rightPresent())
                return@withXPTransfer MethodResult.of(null, abilityExtractResult.right)
            val transferAmount = min(_getStoredXP(), limit)
            adjustStoredXP(-transferAmount)
            abilityExtractResult.left!!.adjustStoredXP(transferAmount)
            MethodResult.of(transferAmount)
        }
    }

    @LuaFunction(mainThread = true)
    fun getStoredXP(): Double {
        return _getStoredXP()
    }

    @LuaFunction(mainThread = true)
    fun getOwnerXP(): MethodResult {
        val player: Player = owner.owner ?: return MethodResult.of(null, "Cannot find owning player")
        return MethodResult.of(player.totalExperience)
    }
}