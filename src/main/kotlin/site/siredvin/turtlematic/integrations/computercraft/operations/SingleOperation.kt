package site.siredvin.turtlematic.integrations.computercraft.operations

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.lib.peripherals.IPeripheralOperation
import java.util.*
import java.util.function.Function

enum class SingleOperation(
    private val defaultCooldown: Int,
    private val distanceCooldownPolicy: DistancePolicy,
    private val countCooldownPolicy: CountPolicy,
    private val defaultCost: Int,
    private val distanceCostPolicy: DistancePolicy,
    private val countCostPolicy: CountPolicy
) : IPeripheralOperation<SingleOperationContext> {
    SWING(1000, 1),
    USE(5000, 1),
    SUCK(1000, 1),
    CAPTURE(50000, 100),
    WARP(
        1000, DistancePolicy.IGNORED, CountPolicy.MULTIPLY,
        1, DistancePolicy.SQRT, CountPolicy.MULTIPLY
    );

    enum class DistancePolicy(private val factorFunction: Function<Int, Int>) {
        IGNORED(Function { 1 }), SQRT(Function { d: Int -> kotlin.math.sqrt(d.toDouble()).toInt() });

        fun getFactor(distance: Int): Int {
            return factorFunction.apply(distance)
        }
    }

    enum class CountPolicy(private val factorFunction: Function<Int, Int>) {
        MULTIPLY(Function { c: Int -> c });

        fun getFactor(count: Int): Int {
            return factorFunction.apply(count)
        }
    }

    private var cooldown: ForgeConfigSpec.IntValue? = null
    private var cost: ForgeConfigSpec.IntValue? = null

    constructor(defaultCooldown: Int, defaultCost: Int) : this(
        defaultCooldown,
        DistancePolicy.IGNORED,
        CountPolicy.MULTIPLY,
        defaultCost,
        DistancePolicy.IGNORED,
        CountPolicy.MULTIPLY
    ) {
    }

    override val initialCooldown: Int
        get() = cooldown!!.get() * countCooldownPolicy.getFactor(5) * distanceCooldownPolicy.getFactor(2)

    override fun getCooldown(context: SingleOperationContext): Int {
        return cooldown!!.get() * countCooldownPolicy.getFactor(context.count) * distanceCooldownPolicy.getFactor(
            context.distance
        )
    }

    override fun getCost(context: SingleOperationContext): Int {
        return cost!!.get() * countCostPolicy.getFactor(context.count) * distanceCostPolicy.getFactor(context.distance)
    }

    override fun computerDescription(): Map<String, Any> {
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["baseCooldown"] = cooldown!!.get()
        data["baseCost"] = cost!!.get()
        data["distanceCooldownPolicy"] = distanceCooldownPolicy.name.lowercase(Locale.getDefault())
        data["countCooldownPolicy"] = countCooldownPolicy.name.lowercase(Locale.getDefault())
        data["distanceCostPolicy"] = distanceCostPolicy.name.lowercase(Locale.getDefault())
        data["countCostPolicy"] = countCostPolicy.name.lowercase(Locale.getDefault())
        return data
    }

    override fun addToConfig(builder: ForgeConfigSpec.Builder) {
        cooldown = builder.defineInRange(
            settingsName() + "Cooldown", defaultCooldown, 1000, Int.MAX_VALUE
        )
        cost = builder.defineInRange(
            settingsName() + "Cost", defaultCost, 0, Int.MAX_VALUE
        )
    }
}