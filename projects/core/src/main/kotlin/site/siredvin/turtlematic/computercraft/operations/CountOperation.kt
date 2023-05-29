package site.siredvin.turtlematic.computercraft.operations

import net.minecraftforge.common.ForgeConfigSpec
import site.siredvin.peripheralium.api.peripheral.IPeripheralOperation
import java.util.*
import java.util.function.Function

enum class CountOperation(
    private val defaultCooldown: Int,
    private val defaultCost: Int,
    private val countCooldownPolicy: CountPolicy = CountPolicy.MULTIPLY,
    private val countCostPolicy: CountPolicy = CountPolicy.MULTIPLY,
) : IPeripheralOperation<Int> {
    SMELT(1, 80),
    CHISEL(1_000, 1, countCooldownPolicy = CountPolicy.IGNORE),
    ;

    enum class CountPolicy(private val factorFunction: Function<Int, Int>) {
        MULTIPLY(Function { c: Int -> c }), IGNORE(Function { 1 });

        fun getFactor(count: Int): Int {
            return factorFunction.apply(count)
        }
    }

    private var cooldown: ForgeConfigSpec.IntValue? = null
    private var cost: ForgeConfigSpec.IntValue? = null

    override val initialCooldown: Int
        get() = cooldown!!.get() * 64

    override fun getCooldown(context: Int): Int {
        return cooldown!!.get() * countCooldownPolicy.getFactor(context)
    }

    override fun getCost(context: Int): Int {
        return cost!!.get() * countCostPolicy.getFactor(context)
    }

    override fun computerDescription(): Map<String, Any?> {
        val data: MutableMap<String, Any?> = HashMap()
        data["name"] = settingsName()
        data["type"] = javaClass.name
        data["baseCooldown"] = cooldown!!.get()
        data["baseCost"] = cost!!.get()
        data["countCooldownPolicy"] = countCooldownPolicy.name.lowercase(Locale.getDefault())
        data["countCostPolicy"] = countCostPolicy.name.lowercase(Locale.getDefault())
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
