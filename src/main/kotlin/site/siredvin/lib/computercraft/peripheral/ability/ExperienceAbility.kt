package site.siredvin.lib.computercraft.peripheral.ability

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
import site.siredvin.lib.api.peripheral.*
import site.siredvin.lib.computercraft.peripheral.operation.UnconditionalOperations
import site.siredvin.lib.util.radiusCorrect
import site.siredvin.lib.util.representation.LuaInterpretation
import kotlin.math.min

class ExperienceAbility(val owner: IPeripheralOwner, private val interactionRadius: Int): IOwnerAbility,
    IPeripheralPlugin {
    companion object {
        private const val COLLECTED_XP_AMOUNT = "CollectedXPAmount"
    }

    override val operations: Array<IPeripheralOperation<*>>
        get() = arrayOf(UnconditionalOperations.XP_TRANSFER)

    override fun collectConfiguration(data: MutableMap<String, Any>) {
        data["xpToFuelRate"] = LibConfig.xpToFuelRate
    }

    fun getStoredXP(): Double {
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
            val oldCount = getStoredXP()
            level.getEntitiesOfClass(ExperienceOrb::class.java, searchBox).forEach { entity ->
                adjustStoredXP(entity.value.toDouble())
                entity.remove(Entity.RemovalReason.KILLED)
            }
            MethodResult.of(getStoredXP() - oldCount)
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
        val burnAmount = min(limit, getStoredXP())
        adjustStoredXP(-burnAmount)
        fuelAbility.addFuel((burnAmount / LibConfig.xpToFuelRate).toInt())
        return burnAmount
    }

    @LuaFunction(mainThread = true)
    @Throws(LuaException::class)
    fun sendXPToOwner(limit: Int): MethodResult {
        return withXPTransfer {
            val count = min(limit.toDouble(), getStoredXP())
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
        val targetPos: BlockPos = LuaInterpretation.asBlockPos(pos, rawBlockPos)
        return withXPTransfer {
            if (!radiusCorrect(pos, targetPos, interactionRadius))
                return@withXPTransfer MethodResult.of(null, "Turtle are too far away")

            val abilityExtractResult =
                AbilityToolkit.extractAbility(PeripheralOwnerAbility.EXPERIENCE, owner.level!!, targetPos)
            if (abilityExtractResult.rightPresent())
                return@withXPTransfer MethodResult.of(null, abilityExtractResult.right)
            val transferAmount = min(getStoredXP(), limit)
            adjustStoredXP(-transferAmount)
            abilityExtractResult.left!!.adjustStoredXP(transferAmount)
            MethodResult.of(transferAmount)
        }
    }

    @LuaFunction(mainThread = true, value = ["getStoredXP"])
    fun getStoredXPLua(): Double {
        return getStoredXP()
    }

    @LuaFunction(mainThread = true)
    fun getOwnerXP(): MethodResult {
        val player: Player = owner.owner ?: return MethodResult.of(null, "Cannot find owning player")
        return MethodResult.of(player.totalExperience)
    }
}