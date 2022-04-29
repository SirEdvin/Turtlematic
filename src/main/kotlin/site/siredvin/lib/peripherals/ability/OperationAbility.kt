package site.siredvin.lib.peripherals.ability

import dan200.computercraft.api.lua.LuaException
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.nbt.CompoundTag
import site.siredvin.lib.LibConfig
import site.siredvin.lib.peripherals.api.*
import site.siredvin.lib.peripherals.api.IPeripheralOwner
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.function.BiConsumer
import java.util.function.Consumer

class OperationAbility(private val owner: IPeripheralOwner) : IOwnerAbility, IPeripheralPlugin {
    private val allowedOperations: MutableMap<String, IPeripheralOperation<*>> = HashMap()

    protected fun setCooldown(operation: IPeripheralOperation<*>, cooldown: Int) {
        if (cooldown > 0) {
            val dataStorage = owner.dataStorage
            if (!dataStorage.contains(COOLDOWNS_TAG)) dataStorage.put(COOLDOWNS_TAG, CompoundTag())
            dataStorage.getCompound(COOLDOWNS_TAG).putLong(
                operation.settingsName(),
                Timestamp.valueOf(LocalDateTime.now().plus(cooldown.toLong(), ChronoUnit.MILLIS)).time
            )
        }
    }

    protected fun getCooldown(operation: IPeripheralOperation<*>): Int {
        val dataStorage = owner.dataStorage
        if (!dataStorage.contains(COOLDOWNS_TAG)) return 0
        val cooldowns = dataStorage.getCompound(COOLDOWNS_TAG)
        val operationName = operation.settingsName()
        if (!cooldowns.contains(operationName)) return 0
        val currentTime = Timestamp.valueOf(LocalDateTime.now()).time
        return Math.max(0, cooldowns.getLong(operationName) - currentTime).toInt()
    }

    fun registerOperation(operation: IPeripheralOperation<*>) {
        allowedOperations[operation.settingsName()] = operation
        if (LibConfig.isInitialCooldownEnabled) {
            val initialCooldown = operation.initialCooldown
            if (initialCooldown >= LibConfig.initialCooldownSensetiveLevel) setCooldown(operation, initialCooldown)
        }
    }

    @Throws(LuaException::class)
    fun <T> performOperation(
        operation: IPeripheralOperation<T>,
        context: T,
        check: IPeripheralCheck<T>?,
        method: IPeripheralFunction<T, MethodResult>,
        successCallback: Consumer<T>?,
        failCallback: BiConsumer<MethodResult?, FailReason?>?
    ): MethodResult {
        if (isOnCooldown(operation)) {
            val result = MethodResult.of(null, String.format("%s is on cooldown", operation.settingsName()))
            failCallback?.accept(result, FailReason.COOLDOWN)
            return result
        }
        if (check != null) {
            val checkResult = check.check(context)
            if (checkResult != null) {
                failCallback?.accept(checkResult, FailReason.CHECK_FAILED)
                return checkResult
            }
        }
        val cost = operation.getCost(context)
        var cooldown = operation.getCooldown(context)
        val fuelAbility: FuelAbility<*>?
        if (cost != 0) {
            fuelAbility = owner.getAbility(PeripheralOwnerAbility.FUEL)
            if (fuelAbility == null) {
                val result = MethodResult.of(null, "This peripheral has no fuel at all")
                failCallback?.accept(result, FailReason.NOT_ENOUGH_FUEL)
                return result
            }
            if (!fuelAbility.consumeFuel(cost, false)) {
                val result = MethodResult.of(null, "Not enough fuel for operation")
                failCallback?.accept(result, FailReason.NOT_ENOUGH_FUEL)
                return result
            }
            cooldown = fuelAbility.reduceCooldownAccordingToConsumptionRate(cooldown)
        }
        val result = method.apply(context)
        successCallback?.accept(context)
        setCooldown(operation, cooldown)
        return result
    }

    fun getCurrentCooldown(operation: IPeripheralOperation<*>): Int {
        return getCooldown(operation)
    }

    fun isOnCooldown(operation: IPeripheralOperation<*>): Boolean {
        return getCurrentCooldown(operation) > 0
    }

    override fun collectConfiguration(dict: MutableMap<String?, Any?>) {
        for (operation in allowedOperations.values) {
            dict[operation.settingsName()] = operation.computerDescription()
        }
    }

    @LuaFunction(mainThread = true, value = ["getCooldown"])
    fun getCooldownLua(name: String): MethodResult {
        val op = allowedOperations[name] ?: return MethodResult.of(null, "Cannot find this operation")
        return MethodResult.of(getCurrentCooldown(op))
    }

    @LuaFunction
    fun getOperations(): List<String> {
        return allowedOperations.keys.toList()
    }

    enum class FailReason {
        COOLDOWN, NOT_ENOUGH_FUEL, CHECK_FAILED
    }

    companion object {
        private const val COOLDOWNS_TAG = "cooldowns"
    }
}