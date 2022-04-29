package site.siredvin.lib.peripherals.ability

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import site.siredvin.lib.peripherals.api.IOwnerAbility
import site.siredvin.lib.peripherals.api.IPeripheralPlugin
import site.siredvin.lib.peripherals.api.IPeripheralOwner

abstract class FuelAbility<T : IPeripheralOwner>(protected var owner: T) : IOwnerAbility, IPeripheralPlugin {
    protected abstract fun _consumeFuel(count: Int): Boolean
    protected abstract val maxFuelConsumptionRate: Int
    protected fun _getFuelConsumptionRate(): Int {
        val settings = owner.dataStorage
        val rate = settings.getInt(FUEL_CONSUMING_RATE_SETTING)
        if (rate == 0) {
            _setFuelConsumptionRate(DEFAULT_FUEL_CONSUMING_RATE)
            return DEFAULT_FUEL_CONSUMING_RATE
        }
        return rate
    }

    protected fun _setFuelConsumptionRate(raw_rate: Int) {
        var rate = raw_rate
        if (rate < DEFAULT_FUEL_CONSUMING_RATE) rate = DEFAULT_FUEL_CONSUMING_RATE
        val maxFuelRate = maxFuelConsumptionRate
        if (rate > maxFuelRate) rate = maxFuelRate
        owner.dataStorage.putInt(FUEL_CONSUMING_RATE_SETTING, rate)
    }

    abstract val isFuelConsumptionDisable: Boolean
    abstract val fuelCount: Int
    abstract val fuelMaxCount: Int
    abstract fun addFuel(count: Int)
    val fuelConsumptionMultiply: Int
        get() = Math.pow(2.0, (_getFuelConsumptionRate() - 1).toDouble()).toInt()

    fun reduceCooldownAccordingToConsumptionRate(cooldown: Int): Int {
        return cooldown / _getFuelConsumptionRate()
    }

    fun consumeFuel(count: Int, simulate: Boolean): Boolean {
        if (isFuelConsumptionDisable) return true
        val realCount = count * fuelConsumptionMultiply
        return if (simulate) fuelLevel >= realCount else _consumeFuel(realCount)
    }

    @get:LuaFunction(mainThread = true)
    val fuelLevel: Int
        get() = fuelCount

    @get:LuaFunction(mainThread = true)
    val maxFuelLevel: Int
        get() = fuelMaxCount

    @get:LuaFunction(mainThread = true)
    val fuelConsumptionRate: Int
        get() = _getFuelConsumptionRate()

    @LuaFunction(mainThread = true)
    fun setFuelConsumptionRate(rate: Int): MethodResult {
        if (rate < 1) return MethodResult.of(null, "Too small fuel consumption rate")
        if (rate > maxFuelConsumptionRate) return MethodResult.of(null, "Too big fuel consumption rate")
        _setFuelConsumptionRate(rate)
        return MethodResult.of(true)
    }

    override fun collectConfiguration(dict: MutableMap<String?, Any?>) {
        dict["maxFuelConsumptionRate"] = maxFuelConsumptionRate
    }

    companion object {
        protected const val FUEL_CONSUMING_RATE_SETTING = "FUEL_CONSUMING_RATE"
        protected const val DEFAULT_FUEL_CONSUMING_RATE = 1
    }
}