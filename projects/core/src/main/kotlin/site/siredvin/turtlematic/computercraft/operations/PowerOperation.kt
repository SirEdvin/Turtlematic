package site.siredvin.turtlematic.computercraft.operations

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import site.siredvin.turtlematic.TurtlematicCore
import java.util.*
import java.util.function.Function

enum class PowerOperation(
    private val defaultCooldown: Int,
    private val defaultCost: Int,
    private val scalePolicy: ScalePolicy,
) : IPeripheralOperation<PowerOperationContext> {
    THROW_POTION(1_000, 10, ScalePolicy.EXP),
    SHOOT(1_000, 10, ScalePolicy.EXP),
    ;
    enum class ScalePolicy(private val factorFunction: Function<Double, Double>) {
        EXP({ d: Double -> kotlin.math.exp(d) }),
        ;

        fun getFactor(power: Double): Double {
            return factorFunction.apply(power)
        }
    }

    private var cooldown: ForgeConfigSpec.IntValue? = null
    private var cost: ForgeConfigSpec.IntValue? = null

    override val initialCooldown: Int
        get() = cooldown!!.get() * 3

    override fun getCooldown(context: PowerOperationContext): Int {
        return cooldown!!.get()
    }

    override fun getCost(context: PowerOperationContext): Int {
        val fullCost = (cost!!.get() * scalePolicy.getFactor(context.power)).toInt()
        TurtlematicCore.LOGGER.info("Real cost $fullCost")
        return (cost!!.get() * scalePolicy.getFactor(context.power)).toInt()
    }

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["cooldown"] = cooldown!!.get()
        data["baseCost"] = cost!!.get()
        data["costScalePolicy"] = scalePolicy.name.lowercase(Locale.getDefault())
        return data
    }

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        cooldown = builder.defineInRange(
            settingsName() + "Cooldown",
            defaultCooldown,
            1000,
            Int.MAX_VALUE,
        )
        cost = builder.defineInRange(
            settingsName() + "Cost",
            defaultCost,
            0,
            Int.MAX_VALUE,
        )
    }
}
